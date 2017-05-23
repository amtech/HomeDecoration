package com.giants3.hd.server.controller;

import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 文件上传控制类
 */
@Controller
@RequestMapping("/erpWork")
public class ErpWorkController extends BaseController {


    @Autowired
    ErpWorkService erpWorkService;


    /**
     * 查询指令单
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/searchZhiling", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Zhilingdan> searchZhilingdan(@RequestParam("osName") String osName, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {


        return erpWorkService.searchZhilingdan(osName, startDate, endDate);
    }

//    /**
//     * 查询订单的排厂记录
//     *
//     * @param os_no
//     * @param prd_no
//     * @return
//     */
//    @RequestMapping(value = "/findOrderItemProcess", method = RequestMethod.GET)
//    @ResponseBody
//    public RemoteData<ErpOrderItemProcess> findOrderItemProcess(@RequestParam("os_no") String os_no, @RequestParam("prd_no") String prd_no, @RequestParam(value = "pVersion", required = false, defaultValue = "") String pversion) {
//
//
//        return wrapData(erpWorkService.findOrderItemProcess(os_no, prd_no, pversion));
//    }



    /**
     * 发送订单流程
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendWorkFlowMessage", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> sendWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user
            , @RequestBody ErpOrderItemProcess orderItemProcess
            , @RequestParam(value = "tranQty") int tranQty
            , @RequestParam(value = "memo") String memo
    ) {


        return erpWorkService.sendWorkFlowMessage(user, orderItemProcess,  tranQty,memo);

    }

    /**
     * 查询订单的排厂记录
     *
     * @param os_no
     * @param itm
     * @return
     */
    @RequestMapping(value = "/getOrderItemProcess", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpOrderItemProcess> getOrderItemProcess(@RequestParam("os_no") String os_no, @RequestParam("itm") int itm
                                                              , @RequestParam("flowStep") int flowStep) {


        return  erpWorkService.getAvailableOrderItemProcess(os_no, itm, flowStep );
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


        return erpWorkService.receiveOrderItemWorkFlow(user, workFlowMsgId);

    }
    /**
     * 查询订单货款的生产进度
     *
     * @param os_no
     * @param prd_no
     * @return
     */
    @RequestMapping(value = "/findOrderItemReport", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpWorkFlowReport> findOrderItemReport(@RequestParam("os_no") String os_no, @RequestParam("itm") int  itm

    ) {
        return erpWorkService.findErpWorkFlowReport(os_no,itm);
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


        return erpWorkService.rejectWorkFlowMessage(user, workFlowMsgId, toWorkFlowStep, reason);

    }

}
