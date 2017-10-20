package com.giants3.hd.server.app.controller;


import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.app.service.AppQuotationService;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.server.utils.Constraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * 报价
 */
@Controller
@RequestMapping("/app/quotation")
public class AppQuotationController extends BaseController {


    @Autowired
    private AppQuotationService quotationService;
    @Autowired
    private ProductRepository productRepository;


    @Value("${deleteQuotationFilePath}")
    private String deleteQuotationFilePath;


    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<Quotation> search(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {

        RemoteData<Quotation> result = quotationService.search(user, searchValue, pageIndex, pageSize);


        return result;

    }


    @RequestMapping(value = "/create", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<QuotationDetail> createTemp(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        QuotationDetail quotationDetail = quotationService.createNew(user);


        return wrapData(quotationDetail);


    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id) {


       return quotationService.loadAQuotationDetail(id);





    }





//    @RequestMapping(value = "/formal", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    RemoteData<QuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id) {
//
//
//        QuotationDetail detail = quotationService.loadAQuotationDetail(id);
//
//
//        if (detail == null)
//            return wrapError("未找到id=" + id + "的报价记录数据");
//
//
//        RemoteData<QuotationDetail> result = wrapData(detail);
//
//
//        return result;
//
//
//    }

}
