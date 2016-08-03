package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.StockOut;
import com.giants3.hd.server.entity.User;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.noEntity.ErpStockOutDetail;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.server.service.StockService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.RemoteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
*  出入库存相关
*/
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController{




    @Autowired
    StockService stockService;



    @RequestMapping(value="/outList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpStockOut> outList(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "salesId", required = false, defaultValue = "-1") long salesId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize)   {

        return  stockService.search(user,key,salesId,pageIndex,pageSize) ;
    }

    @RequestMapping(value="/findOutDetail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpStockOutDetail> findOutDetail(@RequestParam(value = "ck_no" ) String ck_no  )   {

        return  stockService.findDetail(ck_no) ;
    }




    @RequestMapping(value="/out/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ErpStockOutDetail> save(@RequestBody ErpStockOutDetail stockOutDetail)   {


        RemoteData<ErpStockOutDetail>  detailRemoteData=    stockService.saveOutDetail(stockOutDetail);
        return detailRemoteData;
    }
}
