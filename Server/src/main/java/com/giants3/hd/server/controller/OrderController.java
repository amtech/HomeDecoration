package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.FactoryRepository;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Factory;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    RemoteData<ErpOrder> list( @RequestParam(value = "key", required = false, defaultValue = "") String key
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize)   {

        return  erpService.findByKey(key,pageIndex,pageSize) ;
    }


    @RequestMapping(value="/findOrderItems", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderItem> findOrderItems( @RequestParam(value = "orderNo") String orderNo)   {

        return  erpService.findItemsByOrderNo(orderNo) ;
    }

}
