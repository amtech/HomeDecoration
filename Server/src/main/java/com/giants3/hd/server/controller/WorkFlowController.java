package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.utils.RemoteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 生产流程
 */
@Controller
@RequestMapping("/workFlow")
public class WorkFlowController extends BaseController {


    @Autowired
    private WorkFlowService workFlowService;


    /**
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/subTypes", method = RequestMethod.GET)
    public  @ResponseBody
    RemoteData<WorkFlowSubType> getWorkFlowSubTypes() {

        return workFlowService.getWorkFlowSubTypes();
    }

    /**
     * 获取配置的一级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public @ResponseBody RemoteData<WorkFlow> getWorkFlowTypes() {

        return wrapData(workFlowService.getAllWorkFlow());
    }

    /**
     * 根据产品id查询 流程配置信息
     * @param productId
     * @return
     */
    @RequestMapping(value = "/findWorkFlowByProductId", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<WorkFlowProduct> findWorkFlowByProductId(@RequestParam(value = "productId")     long productId) {

        return  workFlowService.findWorkFlowByProductId(productId    );

    }
 /**
     * 保存产品的流程配置信息
     * @param workFlowProduct
     * @return
     */
    @RequestMapping(value = "/saveWorkFlowProduct", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<WorkFlowProduct> saveWorkFlowProduct(@RequestBody WorkFlowProduct workFlowProduct) {

        return  workFlowService.saveWorkFlowProduct(  workFlowProduct   );

    }
/**
     * 保存产品的流程配置信息
     * @param workFlowProduct
     * @return
     */
    @RequestMapping(value = "/startOrderItemWorkFlow", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<OrderItemWorkFlow> startOrderItemWorkFlow(@RequestBody OrderItemWorkFlow workFlowProduct) {

           workFlowService.startOrderItemWorkFlow(  workFlowProduct   );

        return workFlowService.findWorkFlowByOrderItemId(workFlowProduct.orderItemId);

    }




    /**
     * 根据订单货款id查询 流程 信息
     * @param orderItemId
     * @return
     */
    @RequestMapping(value = "/orderItemState", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<OrderItemWorkFlowState> findOrderItemState(@RequestParam(value = "orderItemId")     long orderItemId) {

        return  workFlowService.findOrderItemWorkFlowState(orderItemId    );

    }

}
