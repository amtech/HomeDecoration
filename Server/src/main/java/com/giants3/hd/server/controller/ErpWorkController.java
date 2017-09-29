package com.giants3.hd.server.controller;

import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.server.service.UserService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.*;
import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.entity_erp.Zhilingdan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件上传控制类
 */
@Controller
@RequestMapping("/erpWork")
public class ErpWorkController extends BaseController {


    @Autowired
    ErpWorkService erpWorkService;

    @Autowired
    UserService userService;


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
            , @RequestParam(value = "areaId") long areaId
            , @RequestParam(value = "memo") String memo
    ) {


        final RemoteData<Void> remoteData = erpWorkService.sendWorkFlowMessage(user, orderItemProcess, tranQty, areaId, memo);



        if(remoteData.isSuccess())
        {


            List<User> users=userService.list();


//            for()

        }


        return remoteData;

    }

    /**
     * 查询订单的排厂记录
     *
     * @param os_no
     * @param itm
     * @return
     */
    @RequestMapping(value = "/getAvailableOrderItemProcess", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("os_no") String os_no, @RequestParam("itm") int itm
            , @RequestParam("flowStep") int flowStep) {


        return erpWorkService.getAvailableOrderItemProcess(user, os_no, itm, flowStep);
    }

    /**
     * 接收生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/receiveWorkFlowMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> receiveWorkFlow(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId, @RequestParam("image") MultipartFile[] files) {


        return erpWorkService.receiveOrderItemWorkFlow(user, workFlowMsgId, files, "测试区域");

    }

    /**
     * 接收生产流程递交
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/searchUnCompleteOrderItems", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<ErpOrderItem> searchUnCompleteOrderItems(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "key") String key) {


        return wrapData(erpWorkService.searchUnCompleteOrderItems(key));

    }


    /**
     * 查询订单货款的生产进度
     *
     * @param os_no
     */
    @RequestMapping(value = "/findOrderItemReport", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpWorkFlowReport> findOrderItemReport(@RequestParam("os_no") String os_no, @RequestParam("itm") int itm

    ) {





        return erpWorkService.findErpWorkFlowReport(os_no, itm);
    }


    /**
     * 流程审核拒绝  返工
     * sendWorkFlowMessage?orderItemId=%d%flowStep=%d&tranQty=%d
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/rejectWorkFlowMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> rejectWorkFlowMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "workFlowMsgId") long workFlowMsgId, @RequestParam("image") MultipartFile[] files, @RequestParam(value = "memo") String memo) {
        return erpWorkService.rejectWorkFlowMessage(user, workFlowMsgId, files, memo);

    }


    /**
     * 查询订单的生产备注
     *
     * @param os_no
     * @param itm
     * @return
     */
    @RequestMapping(value = "/getOrderItemWorkMemos", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<OrderItemWorkMemo> getOrderItemWorkMemos(@RequestParam("os_no") String os_no, @RequestParam("itm") int itm

    ) {
        return erpWorkService.getOrderItemWorkMemo(os_no, itm);
    }

    /**
     * 查询 货款的生产备注
     *
     * @param productName
     * @param pVersion
     * @return
     */
    @RequestMapping(value = "/getProductWorkMemos", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ProductWorkMemo> getProductWorkMemos(@RequestParam("productName") String productName, @RequestParam("pVersion") String pVersion

    ) {
        return erpWorkService.getProductWorkMemo(productName, pVersion);
    }


    /**
     * 查询 货款的生产备注
     *
     * @return
     */
    @RequestMapping(value = "/saveWorkMemo", method = RequestMethod.POST)
    @ResponseBody
    public RemoteData<Void> saveWorkMemo(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestBody Map<String, String> data

    ) {


        int workFlowStep = Integer.valueOf(data.get("workFlowStep"));
        String os_no = (String) data.get("os_no");
        int itm = Integer.valueOf(data.get("itm"));
        String orderItemWorkMemo = (String) data.get("orderItemWorkMemo");
        String prd_name = (String) data.get("prd_name");
        String pVersion = (String) data.get("pVersion");
        String productWorkMemo = (String) data.get("productWorkMemo");

        return erpWorkService.saveWorkMemo(user,workFlowStep, os_no, itm, orderItemWorkMemo, prd_name, pVersion, productWorkMemo);
    }

    /**
     * 查询流程的生产材料列表
     *
     * @return
     */
    @RequestMapping(value = "/workFlowMaterials", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<WorkFlowMaterial> getWorkFlowMaterials(@RequestParam("osNo") String osNo, @RequestParam("itm") int itm, @RequestParam("workFlowCode") String workFlowCode

    ) {


        return erpWorkService.getWorkFlowMaterials(osNo, itm, workFlowCode);
    }
}
