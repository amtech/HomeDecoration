package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.User;
import com.giants3.hd.server.noEntity.ErpOrderDetail;
import com.giants3.hd.server.noEntity.OrderReportItem;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
* 产品类别
*/
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{


    @Autowired
    private ErpService erpService;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> list(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false, defaultValue = "") String key  , @RequestParam(value = "salesId", required = false, defaultValue = "-1") long salesId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize)   {

        return  erpService.findByKey(user,key,salesId,pageIndex,pageSize) ;
    }
 @RequestMapping(value="/reportByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> reportByCheckDate(  @RequestParam(value = "key", required = false, defaultValue = "") String key  , @RequestParam(value = "dateStart") String dateStart , @RequestParam(value = "dateEnd") String dateEnd
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize)   {

        return  erpService.findByCheckDate(key,dateStart,dateEnd,pageIndex,pageSize) ;
    }
    @RequestMapping(value="/reportItemByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderReportItem> reportItemByCheckDate( @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "saleId" ,  required = false, defaultValue = "-1") long saleId  , @RequestParam(value = "dateStart") String dateStart , @RequestParam(value = "dateEnd") String dateEnd
        )   {

        return  erpService.findItemByCheckDate(user,saleId,dateStart,dateEnd) ;
    }

    @RequestMapping(value="/findOrderItems", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderItem> findOrderItems( @RequestParam(value = "orderNo") String orderNo,@RequestParam(value = "client",required = false,defaultValue ="") String client)   {

        boolean isFromDesk= ConstantData.CLIENT_DESK.equals(client);
        return  erpService.findItemsByOrderNo(orderNo,isFromDesk) ;
    }
    @RequestMapping(value="/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> findOrderItems(@RequestParam(value = "os_no") String os_no )   {


        return  erpService.getOrderDetail(os_no) ;
    }


    @RequestMapping(value="/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> save(@RequestBody ErpOrderDetail stockOutDetail)   {


        RemoteData<ErpOrderDetail>  detailRemoteData=    erpService.saveOrderDetail(stockOutDetail);
        return detailRemoteData;
    }

}
