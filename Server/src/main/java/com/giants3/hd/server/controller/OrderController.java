package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.User;
import com.giants3.hd.server.entity.WorkFlow;
import com.giants3.hd.server.entity.WorkFlowMessage;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
import com.giants3.hd.server.noEntity.ErpOrderDetail;
import com.giants3.hd.server.noEntity.OrderReportItem;
import com.giants3.hd.server.service.ErpService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.RemoteData;
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
    @RequestMapping(value = "/startOrderTrack", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> startTrack(@RequestParam(value = "os_no") String os_no) {


        RemoteData<Void> detailRemoteData = erpService.startTrack(os_no);
        return detailRemoteData;
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
    @RequestMapping(value = "/unHandleWorkFlowMessage", method = RequestMethod.POST)
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
    @RequestMapping(value = "/receiveWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> receiveWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId) {


        erpService.receiveOrderItemWorkFlow(user, workFlowMsgId);
        return wrapData();
    }



    /**
     * 审核生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/checkWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> checkOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId) {


        erpService.checkOrderItemWorkFlow(user, workFlowMsgId);
        return wrapData();
    }

    /**
     * 发起生产流程 生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendWorkFlow", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Void> sendOrderItemWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "orderItemId") long orderItemId, @RequestParam(value = "toWorkFlowId") long toWorkFlowId) {


        erpService.sendOrderItemWorkFlow(user, orderItemId,toWorkFlowId);
        return wrapData();
    }

}