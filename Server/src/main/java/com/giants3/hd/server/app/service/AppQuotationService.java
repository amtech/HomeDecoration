package com.giants3.hd.server.app.service;

import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.AbstractService;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 广交会报价单
 * Created by davidleen29 on 2017/9/17.
 */
@Service
public class AppQuotationService extends AbstractService {


    @Autowired
    private AppQuotationItemRepository appQuotationItemRepository;
    @Autowired
    private AppQuotationRepository appQuotationRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 新建临时订单
     *
     * @param user
     * @return
     */
    public QuotationDetail createNew(User user) {


        Quotation quotation = new Quotation();
        quotation.qNumber="";
        final Calendar instance = Calendar.getInstance();
        quotation.qDate= DateFormats.FORMAT_YYYY_MM_DD.format(instance.getTime());
        quotation.createTime= instance.getTimeInMillis();
        quotation.saleId=user.id;
        quotation.salesman=user.toString();
        quotation = appQuotationRepository.save(quotation);
        QuotationDetail quotationDetail = new QuotationDetail();
        quotationDetail.quotation = quotation;
        quotationDetail.items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            quotationDetail.items.add(new QuotationItem());
        }

        return quotationDetail;


    }


    public  QuotationDetail getDetail(long quotationId)
    {

        QuotationDetail quotationDetail = new QuotationDetail();

        Quotation quotation = appQuotationRepository.findOne(quotationId);
        List<QuotationItem> quotationItemList = appQuotationItemRepository.findByQuotationIdEqualsOrderByItmAsc(quotationId);
        quotationDetail.quotation = quotation;
        quotationDetail.items = quotationItemList;
        return quotationDetail;
    }

    /**
     * 查询广交会报价单详情
     *
     * @param id
     * @return
     */
    public RemoteData<QuotationDetail> loadAQuotationDetail(long id) {



        return wrapData(getDetail(id));

    }


    /**
     * 广交会报价单查询
     *
     * @param user
     * @param searchValue
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<Quotation> search(User user, String searchValue, int pageIndex, int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue;
        final String key = StringUtils.sqlLike(searchValue);
        pageValue = appQuotationRepository.findByKey(key,  pageable);

        List<Quotation> datas = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), datas);

    }


    /**
     * 往报价单添加产品
     *
     * @param quotationId
     * @param productId
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> addItem(long quotationId, long productId) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByItmAsc(quotationId);


        QuotationItem quotationItem = new QuotationItem();
        quotationItem.quotationId = quotationId;
        bindProductToQuotationItem(quotationItem, productId);
        final int sizeBeforAdd = quotationItems.size();
        quotationItem.itm = sizeBeforAdd;
        appQuotationItemRepository.saveAndFlush(quotationItem);


        updateTotalMessage(quotation);
        quotation.itemCount= sizeBeforAdd +1;

        appQuotationRepository.save(quotation);


        return loadAQuotationDetail(quotationId);
    }


    /**
     * 更新报价产品数据
     *
     * @param quotationId
     * @param productId
     * @param itemIndex
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> updateProduct(long quotationId, int itemIndex, long productId) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        QuotationItem item = appQuotationItemRepository.findFirstByQuotationIdEqualsAndItmEquals(quotationId, itemIndex);


        if (item == null) return wrapError("未找到报价单明细项次：" + itemIndex);


        bindProductToQuotationItem(item, productId);

        appQuotationItemRepository.saveAndFlush(item);


        updateTotalMessage(quotation);

        appQuotationRepository.saveAndFlush(quotation);


        return loadAQuotationDetail(quotationId);
    }


    /**
     * 重新绑定产品数据
     *
     * @param quotationItem
     * @param productId
     */
    private void bindProductToQuotationItem(QuotationItem quotationItem, long productId) {

        Product product = productRepository.findOne(productId);
        quotationItem.productId = productId;
        quotationItem.productName = product.name;
        quotationItem.pVersion = product.pVersion;
        quotationItem.price = product.price;
        quotationItem.priceOrigin = product.price;
        quotationItem.inBoxCount = product.insideBoxQuantity;
        quotationItem.packQuantity = product.packQuantity;
        quotationItem.amountSum = quotationItem.price * quotationItem.qty;
        quotationItem.weight = product.weight;
        quotationItem.weightSum = quotationItem.weight * quotationItem.qty;
        quotationItem.volumePerBox = product.packVolume;
        quotationItem.volumeSum = quotationItem.volumePerBox * quotationItem.qty;
        quotationItem.thumbnail =product.thumbnail;
        quotationItem.photoUrl = product.url;
        quotationItem.unit = product.getpUnitName();
        quotationItem.spec = product.getSpec();
        quotationItem.boxLong = product.packLong;
        quotationItem.boxWidth= product.packWidth;
        quotationItem.boxHeight = product.packHeight;
        quotationItem.weightPerBox = product.weight*product.insideBoxQuantity;
        quotationItem.weight = product.weight ;


    }

    /**
     * 往报价单删除产品
     *
     * @param quotationId
     * @param itemIndex
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> removeItem(long quotationId, int itemIndex) {

        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        QuotationItem item = appQuotationItemRepository.findFirstByQuotationIdEqualsAndItmEquals(quotationId, itemIndex);


        if (item == null) return wrapError("未找到报价单明细项次：" + itemIndex);

        appQuotationItemRepository.delete(item);
        appQuotationItemRepository.flush();

        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByItmAsc(quotationId);

        int index = 1;
        for (QuotationItem aItem : quotationItems) {
            aItem.itm = index++;
            appQuotationItemRepository.saveAndFlush(aItem);
        }


        updateTotalMessage(quotation);
        quotation.itemCount=quotationItems.size();
        appQuotationRepository.saveAndFlush(quotation);


        return loadAQuotationDetail(quotationId);


    }


    /**
     * 更新汇总数据
     */
    private void updateTotalMessage(Quotation quotation) {
        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByItmAsc(quotation.id);

        float totalAmount = 0;
        float totalVolume = 0;
        float totalWeight = 0;


        for (QuotationItem aItem : quotationItems) {

            totalAmount += aItem.amountSum;
            totalVolume += aItem.volumeSum;
            totalWeight += aItem.weightSum;

        }
        quotation.totalAmount = FloatHelper.scale( totalAmount,2) ;
        quotation.totalVolume = totalVolume;
        quotation.totalWeight = totalWeight;

    }

    /**
     * 修改报价款项数量
     *
     * @param quotationId
     * @param itemIndex
     * @param quantity
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> updateItemQuantity(long quotationId, int itemIndex, int quantity) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        QuotationItem quotationItem = appQuotationItemRepository.findFirstByQuotationIdEqualsAndItmEquals(quotationId, itemIndex);


        if (quotationItem == null) return wrapError("未找到报价单明细项次：" + itemIndex);

        quotationItem.qty = quantity;
        quotationItem.amountSum = quotationItem.price * quotationItem.qty;
        quotationItem.packQuantity = quotationItem.qty / (quotationItem.inBoxCount<=0?1:quotationItem.inBoxCount);
        quotationItem.volumeSum = quotationItem.volumePerBox * quotationItem.packQuantity;
        quotationItem.weightSum = quotationItem.weight * quotationItem.qty;
        quotationItem.amountSum = quotationItem.price * quotationItem.qty;
        appQuotationItemRepository.saveAndFlush(quotationItem);


        updateTotalMessage(quotation);
        appQuotationRepository.save(quotation);


        appQuotationRepository.flush();


        return loadAQuotationDetail(quotationId);
    }


    /**
     * 针对报价款项打折
     *
     * @param quotationId
     * @param itemIndex
     * @param discount
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> updateItemDiscount(long quotationId, int itemIndex, float discount) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        QuotationItem quotationItem = appQuotationItemRepository.findFirstByQuotationIdEqualsAndItmEquals(quotationId, itemIndex);


        if (quotationItem == null) return wrapError("未找到报价单明细项次：" + itemIndex);

        quotationItem.price = FloatHelper.scale(quotationItem.priceOrigin * discount);
        quotationItem.amountSum = FloatHelper.scale(quotationItem.price * quotationItem.qty);

        appQuotationItemRepository.saveAndFlush(quotationItem);

        updateTotalMessage(quotation);
        appQuotationRepository.save(quotation);

        appQuotationRepository.flush();


        return loadAQuotationDetail(quotationId);
    }


    /**
     * 针对报价单全打折
     *
     * @param quotationId
     * @param discount
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> updateQuotationDiscount(long quotationId, float discount) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByItmAsc(quotationId);
        float totalAmount = 0;
        for (QuotationItem item : quotationItems) {


            item.price = FloatHelper.scale(item.priceOrigin * discount);
            item.amountSum = FloatHelper.scale( item.price * item.qty);
            totalAmount += item.amountSum;


        }
        appQuotationItemRepository.save(quotationItems);
        appQuotationItemRepository.flush();


        quotation.totalAmount =FloatHelper.scale( totalAmount,2);
        appQuotationRepository.save(quotation);


        appQuotationItemRepository.flush();
        appQuotationRepository.flush();


        return loadAQuotationDetail(quotationId);
    }


    /**
     * 保存报价单（功能只是将临时报价单生产正式报价单）
     *
     * @param quotationId
     * @return
     */
    @Transactional
    public synchronized RemoteData<QuotationDetail> save(long quotationId)

    {

        Quotation quotation = appQuotationRepository.findOne(quotationId);



        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        if(!quotation.formal) {


            //生成流水单号  日期—+流水单号
            String today=DateFormats.FORMATYYYYMMDD.format(Calendar.getInstance().getTime());
            String qNumber="";

            Quotation maxQuotation= appQuotationRepository.findFirstByQNumberLikeOrderByQNumberDesc(StringUtils.sqlRightLike(qNumber));
            if(maxQuotation==null||maxQuotation.qNumber.length()<8||!today.equals(maxQuotation.qNumber.substring(0,8)))
            {
                qNumber=today+"00001";
            }else
            {
                try {
                    qNumber = today + (Integer.valueOf(maxQuotation.qNumber.substring(8)) + 1);
                }catch (Throwable t){
                    qNumber=today+"000001";
                }
            }

            quotation.qNumber=qNumber;
            quotation.formal = true;
            quotation = appQuotationRepository.saveAndFlush(quotation);
        }
        return loadAQuotationDetail(quotationId);


    }


    /**
     * 打印报价单
     *
     * @param quotationId
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> print(long quotationId) {


        return null;

    }

    @Transactional
    public RemoteData<QuotationDetail> updateCustomer(long quotationId, long customerId) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }

        Customer customer = customerRepository.findOne(customerId);
        if (customer == null) {
            return wrapError("客户不存在");
        }


        quotation.customerId = customerId;
        quotation.customerCode = customer.code;
        quotation.customerName = customer.name;
        quotation.customerAddress = customer.addr;
        quotation = appQuotationRepository.save(quotation);
        return loadAQuotationDetail(quotationId);

    }

    @Transactional
    public RemoteData<QuotationDetail> updateMemo(long quotationId, String memo) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }

        memo = memo == null ? "" : memo;
        quotation.memo = memo;

        quotation = appQuotationRepository.save(quotation);
        return loadAQuotationDetail(quotationId);
    }

    @Transactional
    public RemoteData<QuotationDetail> updateSaleman(long quotationId, long saleId) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }

        User user = userRepository.findOne(saleId);
        if (user == null) {
            return wrapError("业务员不存在");
        }


        quotation.saleId = saleId;
        quotation.salesman = user.toString();
        quotation = appQuotationRepository.save(quotation);
        return loadAQuotationDetail(quotationId);

    }




    public RemoteData<QuotationDetail> updateItemPrice(long quotationId, int itemIndex, float price) {


        Quotation quotation = appQuotationRepository.findOne(quotationId);


        if (quotation == null) {

            return wrapError("报价单不存在");
        }


        QuotationItem quotationItem = appQuotationItemRepository.findFirstByQuotationIdEqualsAndItmEquals(quotationId, itemIndex);


        if (quotationItem == null) return wrapError("未找到报价单明细项次：" + itemIndex);

        quotationItem.price = price;
        quotationItem.amountSum = quotationItem.price * quotationItem.qty;

        appQuotationItemRepository.saveAndFlush(quotationItem);

        updateTotalMessage(quotation);
        appQuotationRepository.save(quotation);

        appQuotationRepository.flush();


        return loadAQuotationDetail(quotationId);

    }
}
