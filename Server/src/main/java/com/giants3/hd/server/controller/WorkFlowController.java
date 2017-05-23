package com.giants3.hd.server.controller;


import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 获取配置的二级流程类型数据
     *
     * @return
     */
    @RequestMapping(value = "/saveSubTypes", method = RequestMethod.POST)
    public  @ResponseBody
    RemoteData<WorkFlowSubType> saveSubTypes(@RequestBody List<WorkFlowSubType> workFlowSubTypes) {

        return workFlowService.saveSubTypes(workFlowSubTypes);
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
    public  @ResponseBody RemoteData<OrderItemWorkFlow> startOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestBody OrderItemWorkFlow workFlowProduct) {

            RemoteData<OrderItemWorkFlow>      result=     workFlowService.startOrderItemWorkFlow( user, workFlowProduct   );
        return result;



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



    /**
     * 查询流程工作人表
     * @return
     */
    @RequestMapping(value = "/workers", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<WorkFlowWorker> findWorkers( ) {

        return  workFlowService.findWorkers(     );

    }
    /**
     * 查询流程工作人表
     * @return
     */
    @RequestMapping(value = "/arrangers", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<WorkFlowArranger> findArrangers( ) {

        return  workFlowService.findArrangers(     );

    }
    /**
     * 查询流程事件列表
     * @return
     */
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<WorkFlowEvent> findWorkFlowEvents( ) {

        return  workFlowService.findWorkFlowEvents(     );


    }   /**
     * 查询流程事件工作人表
     * @return
     */
    @RequestMapping(value = "/eventWorkers", method = RequestMethod.GET)
    public  @ResponseBody RemoteData<WorkFlowEventWorker> findWorkFlowEventWorkers( ) {

        return  workFlowService.findWorkFlowEventWorkers(     );


    }
/**
     * 保存流程工作人表
     * @return
     */
    @RequestMapping(value = "/saveWorker", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<WorkFlowWorker> saveWorker(@RequestBody WorkFlowWorker workFlowWorker ) {

        return  workFlowService.saveWorker(  workFlowWorker   );

    }

/**
     * 保存排厂工作人表
     * @return
     */
    @RequestMapping(value = "/saveArranger", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<WorkFlowArranger> saveArranger(@RequestBody WorkFlowArranger workFlowWorker ) {

        return  workFlowService.saveArranger(  workFlowWorker   );

    }

/**
     * 删除排厂工作人表
     * @return
     */
    @RequestMapping(value = "/deleteArranger", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<Void> deleteArranger(@RequestParam(value = "id")     long id) {

        return  workFlowService.deleteWorkFlowArranger(  id   );

    }

/**
     * 删除流程节点工作人
     * @return
     */
    @RequestMapping(value = "/deleteWorker", method = RequestMethod.POST)
    public  @ResponseBody RemoteData<Void> deleteWorker(@RequestParam(value = "id")     long id) {

        return  workFlowService.deleteWorkFlowWorker(  id   );

    }

    /**读取指定订单流程，流程的消息列表
     *  cancelOrderWorkFlow?orderItemWorkFlowId
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/workFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getOrderItemWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user


            , @RequestParam(value = "os_no" ) String os_no
            , @RequestParam(value = "itm" ) int itm
            , @RequestParam(value = "workFlowStep" ) int workFlowStep


    ) {


        return workFlowService.getOrderItemWorkFlowMessage(user,  os_no,  itm,workFlowStep);



    }



}
