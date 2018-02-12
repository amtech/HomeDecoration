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
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id",   defaultValue = "0") long id) {


       return quotationService.loadAQuotationDetail(id);





    }

    /**
     * 保存报价单（功能只是将临时报价单生产正式报价单）
     * @param id
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> save(@RequestParam(value = "quotationId",   defaultValue = "0") long id) {


        return quotationService.save(id);





    }
  /**
     * 修改报价单产品
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateProduct(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "itemIndex" ) int itemIndex,@RequestParam(value = "productId" ) long productId) {


        return quotationService.updateProduct(quotationId,itemIndex,productId);





    } /**
     * 修改报价单产品
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateItemQuantity", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemQuantity(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "itemIndex" ) int itemIndex,@RequestParam(value = "quantity" ) int quantity) {


        return quotationService.updateItemQuantity(quotationId,itemIndex,quantity);





    }

    /**
     * 修改报价单产品折扣
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateItemDiscount", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemDiscount(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "itemIndex" ) int itemIndex,@RequestParam(value = "discount" ) float discount) {


        return quotationService.updateItemDiscount(quotationId,itemIndex,discount);





    }/**
     * 修改报价单全部折扣
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/updateQuotationDiscount", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateQuotationDiscount(@RequestParam(value = "quotationId" ) long quotationId, @RequestParam(value = "discount" ) float discount) {


        return quotationService.updateQuotationDiscount(quotationId,discount);





    }

    /**
     * 删除报价单中产品
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/removeItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> removeItem(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "itemIndex" ) int itemIndex) {


        return quotationService.removeItem(quotationId,itemIndex);





    }    /**
     * 添加报价单中产品
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> addItem(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "productId" ) int productId) {


        return quotationService.addItem(quotationId,productId);





    }




    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> print(@RequestParam(value = "quotationId" ) long quotationId) {


        quotationService.print(quotationId);

        return wrapData();





    }


    @RequestMapping(value = "/updateMemo", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateMemo(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "memo" ) String  memo) {


      return  quotationService.updateMemo(quotationId,memo);







    }


    @RequestMapping(value = "/updateCustomer", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateCustomer(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "customerId" ) long  customerId) {


        quotationService.updateCustomer(quotationId,customerId);

        return wrapData();





    }



    @RequestMapping(value = "/updateSaleman", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateSaleman(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "saleId" ) long  saleId) {


        quotationService.updateSaleman(quotationId,saleId);

        return wrapData();





    }



    @RequestMapping(value = "/updateItemPrice", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> updateItemPrice(@RequestParam(value = "quotationId" ) long quotationId,@RequestParam(value = "itemIndex" ) int item, @RequestParam(value = "price" ) float price  ) {


        return  quotationService.updateItemPrice(quotationId,item,price);






    }
}
