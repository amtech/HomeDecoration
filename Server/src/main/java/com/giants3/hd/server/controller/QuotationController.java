package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

/**
 * 报价
 */
@Controller
@RequestMapping("/quotation")
public class QuotationController extends BaseController {


    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;
    @Autowired
    private QuotationItemRepository quotationItemRepository;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Quotation> listPrdtJson() {


        return wrapData(quotationRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Quotation> search(@RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue,@RequestParam(value = "salesmanId", required = false, defaultValue ="-1") long salesmanId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue;
        if(salesmanId<0) {
             pageValue = quotationRepository.findByCustomerNameLikeOrQNumberLikeOrderByQNumberDesc("%" + searchValue + "%", "%" + searchValue + "%", pageable);
        }else
        {
            pageValue= quotationRepository.findByCustomerNameLikeAndSalesmanIdEqualsOrQNumberLikeAndSalesmanIdEqualsOrderByQNumberDesc("%" + searchValue + "%", salesmanId, "%" + searchValue + "%", salesmanId, pageable);

        }
        List<Quotation> products = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }



    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id)
    {
     Quotation quotation=   quotationRepository.findOne(id);

        if(quotation==null)
            return wrapError("未找到id="+id+"的报价记录数据");

        QuotationDetail detail=new QuotationDetail();
        detail.quotation=quotation;
        detail.items=  quotationItemRepository.findByQuotationIdEquals(id);

        detail.XKItems=quotationXKItemRepository.findByQuotationIdEquals(id);



        return wrapData(detail);
    }




    /**
     * 产品完整信息的保存
     *
     * @param quotationDetail 报价单全部信息
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<QuotationDetail> saveQuotationDetail(@RequestBody QuotationDetail quotationDetail) {


        Quotation newQuotation=quotationDetail.quotation;

        if(!quotationRepository.exists(newQuotation.id))
        {
            //检查单号唯一性
            if(quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber)!=null)
            {

                return   wrapError("报价单号:"+newQuotation.qNumber
                        +"已经存在,请更换");
            }

            newQuotation.id=-1;
        }else {


            Quotation oldQuotation = quotationRepository.findOne(newQuotation.id);

            if(!oldQuotation.qNumber.equals(newQuotation.qNumber))
            {

                //检查单号唯一性
                if(quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber)!=null)
                {

                    return   wrapError("报价单号:"+newQuotation.qNumber
                            +"已经存在,请更换");
                }

            }


            newQuotation.id=oldQuotation.id;
        }


     Quotation savedQuotation=   quotationRepository.save(newQuotation);

        long newId=savedQuotation.id;

        for(QuotationItem item:quotationDetail.items)
        {

            item.quotationId= newId;
            quotationItemRepository.save(item);
        }

        for(QuotationXKItem item:quotationDetail.XKItems)
        {

            item.quotationId= newId;
            quotationXKItemRepository.save(item);
        }


        return detail(newId);


    }


    /**
     *删除产品信息
     *
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData< Void> logicDelete(@RequestParam("id") long quotationId ) {

        //检查是否有关联数据




        quotationRepository.delete(quotationId);

       int affectedRow= quotationItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationItemRepository delete affectedRow:" + affectedRow);
          affectedRow= quotationXKItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationXKItemRepository delete affectedRow:" + affectedRow);
        return wrapData();

    }
}
