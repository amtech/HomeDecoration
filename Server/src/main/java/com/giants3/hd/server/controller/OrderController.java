package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.utils.entity.ErpOrder;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.server.noEntity.ErpOrderDetail;
import com.giants3.hd.server.noEntity.OrderReportItem;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlowReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {


    @Autowired
    private ErpService erpService;
    @Autowired
    private WorkFlowService workFlowService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> list(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "salesId", required = false, defaultValue = "-1") long salesId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return erpService.findByKey(user, key, salesId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/reportByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrder> reportByCheckDate(@RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "dateStart") String dateStart, @RequestParam(value = "dateEnd") String dateEnd
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {

        return erpService.findByCheckDate(key, dateStart, dateEnd, pageIndex, pageSize);
    }

    @RequestMapping(value = "/reportItemByCheckDate", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderReportItem> reportItemByCheckDate(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "saleId", required = false, defaultValue = "-1") long saleId, @RequestParam(value = "dateStart") String dateStart, @RequestParam(value = "dateEnd") String dateEnd
    ) {

        return erpService.findItemByCheckDate(user, saleId, dateStart, dateEnd);
    }


    @RequestMapping(value = "/searchOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItem> searchOrderItem(@RequestParam(value = "key") String key , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {


        return erpService.searchOrderItem(key,pageIndex,pageSize);
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> findOrderItems(@RequestParam(value = "os_no") String os_no) {


        return erpService.getOrderDetail(os_no);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<ErpOrderDetail> save(@RequestBody ErpOrderDetail stockOutDetail) {


        RemoteData<ErpOrderDetail> detailRemoteData = erpService.saveOrderDetail(stockOutDetail);
        return detailRemoteData;
    }


    /**
     * 启动订单跟踪
     *
     * @param
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/startOrderTrack", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> startTrack(@RequestParam(value = "os_no") String os_no) {


//        RemoteData<Void> detailRemoteData = erpService.startTrack(os_no);
//        return detailRemoteData;
        return null;
    }


    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/listWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlow> listWorkFlow() {


        RemoteData<WorkFlow> remoteData = erpService.getWorkFlowList();
        return remoteData;
    }

    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveWorkFlow", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<WorkFlow> saveWorkFlow(@RequestBody List<WorkFlow> workFlowList) {


        RemoteData<WorkFlow> remoteData = erpService.saveWorkFlowList(workFlowList);
        return remoteData;
    }


    /**
     * 获取生产流程表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/unHandleWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getUnHandleWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        RemoteData<WorkFlowMessage> remoteData = erpService.getUnHandleWorkFlowMessage(user);
        return remoteData;
    }


    /**
     * 接收生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/receiveWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> receiveWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId) {


        return erpService.receiveOrderItemWorkFlow(user, workFlowMsgId);

    }


    /**
     * 审核生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/checkWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> checkOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId) {


        return erpService.checkOrderItemWorkFlow(user, workFlowMsgId);

    }


    /**
     * 获取可以发送流程的订单货款
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getOrderItemForTransform", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlowState> getOrderItemForTransform(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        return erpService.getOrderItemForTransform(user);

    }



    /**
     * 获取制定订单的流程生产状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkState", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowReport> getOrderItemWorkState(@RequestParam(value = "orderItemId") long orderItemId) {


        return erpService.getOrderItemWorkState(orderItemId);

    }

    /**
     * 发送订单流程
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> sendWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "orderItemWorkFlowStateId") long orderItemWorkFlowStateId

            , @RequestParam(value = "tranQty") int tranQty
            , @RequestParam(value = "memo") String memo
    ) {


        return erpService.sendWorkFlowMessage(user, orderItemWorkFlowStateId,  tranQty,memo);

    }




    /**
     * 流程审核拒绝  返工
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/rejectWorkFlowMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> rejectWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "workFlowMsgId") long workFlowMsgId
            , @RequestParam(value = "toWorkFlowStep") int toWorkFlowStep
            , @RequestParam(value = "reason") String reason
    ) {


        return erpService.rejectWorkFlowMessage(user, workFlowMsgId, toWorkFlowStep, reason);

    }
    /**
     * 获取可以发送流程的订单货款
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getSendWorkFlowMessageList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<WorkFlowMessage> getSendWorkFlowMessageList(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

    ) {


        return erpService.getSendWorkFlowMessageList(user);

    }


    /**
     * 获取未完成的订单货款
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/unCompleteOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlowState> getUnCompleteOrderItem(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user   ) {


        return erpService.getUnCompleteOrderItem(user);

    }

    /**
     * 获取 订单货款 生产进度数据
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getWorkFlowOrderItem", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlowState> getWorkFlowOrderItem(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "key") String key
            , @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex
            , @RequestParam(value = "pageSize",defaultValue ="20") int pageSize


    ) {


        return erpService.getWorkFlowOrderItem(user,key,pageIndex,pageSize);



    }



    /**
     * 查询 订单货款 生产进度数据
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchOrderItemWorkFlowState", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlowState> searchOrderItemWorkFlowState(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestParam(value = "key") String key
            , @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex
            , @RequestParam(value = "pageSize",defaultValue ="20") int pageSize


    ) {


        return workFlowService.searchOrderItemWorkFlowState(user,key,pageIndex,pageSize);



    } /**
     * //获取关联的流程信息
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkFlowState", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlowState> getOrderItemWorkFlowState(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

            , @RequestParam(value = "orderItemId" ) long orderItemId
            , @RequestParam(value = "workFlowStep" ) int workFlowStep


    ) {


        return workFlowService.getOrderItemWorkFlowState(orderItemId,workFlowStep);



    }
/**
     *  获取订单的流程配置
     *
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderItemWorkFlow> getOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user

            , @RequestParam(value = "orderItemId" ) long orderItemId



    ) {


        return workFlowService.getOrderItemWorkFlow(orderItemId);



    }

}