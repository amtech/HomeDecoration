package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.noEntity.ErpStockOutDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 客户  业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class StockService extends AbstractService {


    EntityManager manager;

    ErpStockOutRepository repository;


    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockOutItemRepository stockOutItemRepository;
    @Autowired
    StockOutAuthRepository stockOutAuthRepository;

    @Autowired
    StockOutRepository stockOutRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;

    @Override
    public void destroy() throws Exception {
        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        repository = new ErpStockOutRepository(manager);
    }


    /**
     * 查询出库列表
     *
     * @param loginUser
     * @param key
     * @param salesId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpStockOut> search(User loginUser, String key, long salesId, int pageIndex, int pageSize) {


        List<String> salesNos = null;
        //查询所有
        StockOutAuth stockOutAuth = stockOutAuthRepository.findFirstByUser_IdEquals(loginUser.id);
        if (stockOutAuth != null && !StringUtils.isEmpty(stockOutAuth.relatedSales)) {
            salesNos = userService.extractUserCodes(loginUser.id, salesId, stockOutAuth.relatedSales);
        }


        if (salesNos == null || salesNos.size() == 0) return wrapData();


        List<ErpStockOut> erpStockOuts = repository.stockOutList(key, salesNos, pageIndex, pageSize);
        for (ErpStockOut stockOut : erpStockOuts) {
            attachData(stockOut);
            attachSaleData(stockOut);
        }


        RemoteData<ErpStockOut> remoteData = new RemoteData<>();


        remoteData.datas = erpStockOuts;
        int totalCount = repository.getRecordCountByKey(key, salesNos);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, erpStockOuts);

    }


    /**
     * 查询出库列表
     *
     * @param ck_no
     * @return
     */
    public RemoteData<ErpStockOutDetail> findDetail(String ck_no) {


        List<ErpStockOutItem> erpStockOuts = repository.stockOutItemsList(ck_no);


        //存放 拆分出来的单项数据
        List<ErpStockOutItem> additionalItems = new ArrayList<>();
        for (ErpStockOutItem item : erpStockOuts) {
            String productCode = item.prd_no;
            String pVersion = "";
            try {
                pVersion = item.id_no.substring(productCode.length() + 2);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            item.pVersion = pVersion;

            //更新相关联的产品信息
            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(productCode, pVersion);
            if (product != null) {

                item.url = product.url;
                item.unit = product.pUnitName;
            }


            //附件封签号 柜号 描述数据


            List<StockOutItem> stockOutItems = stockOutItemRepository.findByCkNoEqualsAndItmEquals(item.ck_no, item.itm);

            if (stockOutItems != null && stockOutItems.size() > 0) {

                int size = stockOutItems.size();
                StockOutItem stockOutItem = stockOutItems.get(0);
                item.describe = stockOutItem.describe;
                item.fengqianhao = stockOutItem.fengqianhao;
                item.guihao = stockOutItem.guihao;
                item.guixing=stockOutItem.guixing;
                item.subRecord = stockOutItem.subRecord;

                //保证 出库的数量有数据
                item.stockOutQty = stockOutItem.stockOutQty == 0 ? item.qty : stockOutItem.stockOutQty;
                item.id = stockOutItem.id;
                //描述取值
                if (!StringUtils.isEmpty(stockOutItem.describe)) {
                    item.describe = stockOutItem.describe;
                } else {
                    item.describe = item.idx_name;
                }

                for (int i = 1; i < size; i++) {
                    ErpStockOutItem additionItem = GsonUtils.fromJson(GsonUtils.toJson(item), ErpStockOutItem.class);
                    stockOutItem = stockOutItems.get(i);
                    additionItem.describe = stockOutItem.describe;
                    additionItem.fengqianhao = stockOutItem.fengqianhao;
                    additionItem.guihao = stockOutItem.guihao;
                    additionItem.guixing=stockOutItem.guixing;
                    additionItem.stockOutQty = stockOutItem.stockOutQty;
                    item.subRecord = stockOutItem.subRecord;
                    additionItem.id = stockOutItem.id;

                    //描述取值
                    if (!StringUtils.isEmpty(stockOutItem.describe)) {
                        additionItem.describe = stockOutItem.describe;
                    } else {
                        additionItem.describe = additionItem.idx_name;
                    }

                    additionalItems.add(additionItem);
                }
            } else {
                item.stockOutQty = item.qty;
            }


        }

        //汇总拆分的数据
        erpStockOuts.addAll(additionalItems);

        //排序
        ArrayUtils.SortList(erpStockOuts, new Comparator<ErpStockOutItem>() {
            @Override
            public int compare(ErpStockOutItem o1, ErpStockOutItem o2) {

                int value = o1.ck_no.compareTo(o2.ck_no);
                if (value != 0) return value;
                value = Integer.compare(o1.itm, o2.itm);
                if (value != 0) return value;
                return Boolean.compare(o2.subRecord,o1.subRecord);

            }
        });


        ErpStockOutDetail detail = new ErpStockOutDetail();
        ArrayList<ErpStockOutDetail> list = new ArrayList();
        list.add(detail);


        ErpStockOut erpStockOut = repository.findStockOut(ck_no);
        attachData(erpStockOut);
        attachSaleData(erpStockOut);
        RemoteData<ErpStockOutDetail> remoteData = new RemoteData<>();

        detail.erpStockOut = erpStockOut;
        detail.items = erpStockOuts;
        remoteData.datas = list;
        return remoteData;
    }


    /**
     * 附加数据
     *
     * @param erpStockOut
     */
    private void attachData(ErpStockOut erpStockOut) {


        if (erpStockOut != null) {


            StockOut stockOut = stockOutRepository.findFirstByCkNoEquals(erpStockOut.ck_no);
            if (stockOut != null) {
                erpStockOut.cemai = stockOut.cemai;
                erpStockOut.zhengmai = stockOut.zhengmai;
                erpStockOut.neheimai = stockOut.neheimai;
                erpStockOut.memo = stockOut.memo;

                erpStockOut.attaches = stockOut.attaches;
            }

        }
    }

    /**
     * 附加业务员数据
     *
     * @param erpStockOut
     */
    private void attachSaleData(ErpStockOut erpStockOut) {


        if (erpStockOut != null) {


            User user = userRepository.findFirstByCodeEquals(erpStockOut.sal_no);
            if (user != null) {
                erpStockOut.sal_name = user.name;
                erpStockOut.sal_cname = user.chineseName;

            }

        }
    }

    /**
     * 剥离数据
     *
     * @param erpStockOut
     */
    private void detachData(ErpStockOut erpStockOut, StockOut stockOut) {
        if (erpStockOut == null || stockOut == null) {
            return;
        }


        stockOut.cemai = erpStockOut.cemai;
        stockOut.zhengmai = erpStockOut.zhengmai;
        stockOut.neheimai = erpStockOut.neheimai;
        stockOut.memo = erpStockOut.memo;
        stockOut.ckNo = erpStockOut.ck_no;
        stockOut.attaches = erpStockOut.attaches;


    }


    /**
     * 保存出库单
     *
     * @param stockOutDetail
     * @return
     */
    @Transactional
    public RemoteData<ErpStockOutDetail> saveOutDetail(ErpStockOutDetail stockOutDetail) {


        StockOut stockOut = stockOutRepository.findFirstByCkNoEquals(stockOutDetail.erpStockOut.ck_no);
        if (stockOut == null) {
            stockOut = new StockOut();
        }


        String oldAttaches = stockOut.attaches;

        detachData(stockOutDetail.erpStockOut, stockOut);
        //附件处理
        stockOut.attaches = AttachFileUtils.updateProductAttaches(stockOut.attaches, oldAttaches, "StockOut_" + stockOut.ckNo, attachfilepath, tempFilePath);
        stockOutRepository.save(stockOut);


        List<ErpStockOutItem> newStockOutItems = stockOutDetail.items;

        List<StockOutItem> oldItems = stockOutItemRepository.findByCkNoEquals(stockOutDetail.erpStockOut.ck_no);
        List<Long> oldIds = new ArrayList<>();
        for (StockOutItem item : oldItems) {
            oldIds.add(item.id);
        }
        for (ErpStockOutItem item : newStockOutItems) {
            oldIds.remove(item.id);
            StockOutItem stockOutItem = stockOutItemRepository.findOne(item.id);
            if (stockOutItem == null) {
                stockOutItem = new StockOutItem();
            }
            stockOutItem.ckNo = item.ck_no;
            stockOutItem.itm = item.itm;
            stockOutItem.describe = item.describe;
            stockOutItem.fengqianhao = item.fengqianhao;
            stockOutItem.guihao = item.guihao;
            stockOutItem.guixing=item.guixing;
            stockOutItem.subRecord = item.subRecord;
            stockOutItem.stockOutQty = item.stockOutQty;
            stockOutItemRepository.save(stockOutItem);
        }

        //删除被删除的记录  不存在新的列表中
        for (Long id : oldIds) {
            stockOutItemRepository.delete(id);
        }


        return findDetail(stockOutDetail.erpStockOut.ck_no);


    }


}
