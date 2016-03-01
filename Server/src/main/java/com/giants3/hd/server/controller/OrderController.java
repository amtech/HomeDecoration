package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.FactoryRepository;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Factory;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    RemoteData<ErpOrder> list( )   {

        return  erpService.findByKey("",0,20) ;
    }




}
