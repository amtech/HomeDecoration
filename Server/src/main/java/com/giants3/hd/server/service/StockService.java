package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.noEntity.ErpStockOutDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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




    /** 查询出库列表
     *
     * @param loginUser
     * @param key
     * @param salesId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpStockOut> search(User loginUser,String key,long salesId, int pageIndex, int pageSize) {

        List<String> salesNos=null;
        //查询所有
        StockOutAuth stockOutAuth=      stockOutAuthRepository.findFirstByUser_IdEquals(loginUser.id);
        if(stockOutAuth!=null&& !StringUtils.isEmpty(stockOutAuth.relatedSales)) {
            salesNos = userService.extractUserCodes(loginUser.id, salesId,stockOutAuth.relatedSales);
        }


        if(salesNos==null||salesNos.size()==0 ) return wrapData();


        List<ErpStockOut> erpStockOuts = repository.stockOutList(key,salesNos, pageIndex, pageSize);
        for (ErpStockOut stockOut : erpStockOuts) {
            attachData(stockOut);
            attachSaleData(stockOut);
        }


        RemoteData<ErpStockOut> remoteData = new RemoteData<>();


        remoteData.datas = erpStockOuts;
        int totalCount = repository.getRecordCountByKey(key,salesNos);
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


            StockOutItem stockOutItem=stockOutItemRepository.findFirstByCkNoEqualsAndItmEquals(item.ck_no,item.itm);
            if(stockOutItem!=null)
            {

                item.describe=stockOutItem.describe;
                item.fengqianhao=stockOutItem.fengqianhao;
                item.guihao=stockOutItem.guihao;

            }






        }


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


            User user = userRepository.findFirstByCodeEquals(erpStockOut.sal_no );
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
        stockOut.attaches = AttachFileUtils.updateProductAttaches(stockOut.attaches, oldAttaches,"StockOut_"+stockOut.ckNo, attachfilepath, tempFilePath);
        stockOutRepository.save(stockOut);



        List<ErpStockOutItem> newStockOutItems=stockOutDetail.items;

        for(ErpStockOutItem item :newStockOutItems)
        {

            StockOutItem stockOutItem=stockOutItemRepository.findFirstByCkNoEqualsAndItmEquals(item.ck_no,item.itm);
            if(stockOutItem==null)
            {
                stockOutItem=new StockOutItem();
            }
            stockOutItem.ckNo=item.ck_no;
            stockOutItem.itm=item.itm;
            stockOutItem.describe=item.describe;
            stockOutItem.fengqianhao=item.fengqianhao;
            stockOutItem.guihao=item.guihao;

            stockOutItemRepository.save(stockOutItem);
        }






        return findDetail(stockOutDetail.erpStockOut.ck_no);


    }


}
