package com.giants3.hd.server.app.service;

import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.repository.AppQuotationItemRepository;
import com.giants3.hd.server.repository.AppQuotationRepository;
import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.server.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /**
     * 新建临时订单
     *
     * @param user
     * @return
     */
    public QuotationDetail createNew(User user) {


        Quotation quotation = new Quotation();
        quotation = appQuotationRepository.save(quotation);


        QuotationDetail quotationDetail = new QuotationDetail();
        quotationDetail.quotation = quotation;
        quotationDetail.items = new ArrayList<>();

        return quotationDetail;


    }


    /**
     * 查询广交会报价单详情
     *
     * @param id
     * @return
     */
    public RemoteData<QuotationDetail> loadAQuotationDetail(long id) {


        QuotationDetail quotationDetail = new QuotationDetail();
        Quotation quotation = appQuotationRepository.findOne(id);
        List<QuotationItem> quotationItemList = appQuotationItemRepository.findByQuotationIdEqualsOrderByIndexAsc(id);
        quotationDetail.quotation = quotation;
        quotationDetail.items = quotationItemList;
        return wrapData(quotationDetail);

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
        pageValue = appQuotationRepository.findByCustomerLikeOrQNumberLikeOrderByQDateDesc("%" + searchValue + "%", "%" + searchValue + "%", pageable);

        List<Quotation> datas = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), datas);

    }


    /**
     * 往报价单添加产品
     *
     * @param quotatinId
     * @param productId
     * @param index
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> addProduct(long quotationId, long productId ) {

        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByIndexAsc(quotationId);


        QuotationItem quotationItem=new QuotationItem();
        quotationItem.quotationId=quotationId;
        bindProductToQuotationItem(quotationItem,productId);
        quotationItem.index=quotationItems.size();
        quotationItem=  appQuotationItemRepository.save(quotationItem);





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
    public RemoteData<QuotationDetail> updateProduct(long quotationId, int itemIndex,long productId ) {

        QuotationItem quotationItem = appQuotationItemRepository.findFirstByQuotationIdEqualsAndIndexEquals(quotationId,itemIndex);


        bindProductToQuotationItem(quotationItem,productId);

        quotationItem=  appQuotationItemRepository.save(quotationItem);





        return loadAQuotationDetail(quotationId);
    }


    /**
     * 重新绑定产品数据
     * @param quotationItem
     * @param productId
     */
    private void bindProductToQuotationItem(QuotationItem quotationItem, long productId)
    {

        Product product=productRepository.findOne(productId);
        quotationItem.productId=productId;
        quotationItem.productName=product.name;
        quotationItem.pVersion=product.pVersion;
        quotationItem.price=product.price;
        quotationItem.orignPrice=product.price;


    }

    /**
     * 往报价单添加产品
     *
     * @param quotationId
     * @param itemIndex
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> removeProduct(long quotationId, int itemIndex) {


        QuotationItem item = appQuotationItemRepository.findFirstByQuotationIdEqualsAndIndexEquals(quotationId, itemIndex);
        if (item != null) appQuotationItemRepository.delete(item);


        List<QuotationItem> quotationItems = appQuotationItemRepository.findByQuotationIdEqualsOrderByIndexAsc(quotationId);

        int index = 1;
        for (QuotationItem aItem : quotationItems) {
            aItem.index = index++;
            appQuotationItemRepository.save(aItem);

        }


        return loadAQuotationDetail(quotationId);


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



        return loadAQuotationDetail(quotationId);
    }


    /**
     * 更新报价单基础信息
     *
     * @param quotation

     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> updateQuotation(long quotationId,Quotation quotation) {





        return loadAQuotationDetail(quotationId);
    }


    /**
     * 保存数据
     * @param quotation
     * @return
     */
    @Transactional
    public RemoteData<QuotationDetail> save(Quotation quotation)

    {


        return null;

    }
}
