package com.giants3.hd.server.controller;

import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.ErpWorkFlowReport;
import com.giants3.hd.utils.entity.ErpOrderItemProcess;
import com.giants3.hd.utils.entity.Zhilingdan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 查询订单的排厂记录
     *
     * @param os_no
     * @param prd_no
     * @return
     */
    @RequestMapping(value = "/findOrderItemProcess", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpOrderItemProcess> findOrderItemProcess(@RequestParam("os_no") String os_no, @RequestParam("prd_no") String prd_no) {


        return erpWorkService.findOrderItemProcess(os_no, prd_no);
    }/**
     * 查询订单货款的生产进度
     *
     * @param os_no
     * @param prd_no
     * @return
     */
    @RequestMapping(value = "/findOrderItemReport", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<ErpWorkFlowReport> findOrderItemReport(@RequestParam("os_no") String os_no, @RequestParam("prd_no") String prd_no) {
        return erpWorkService.findErpWorkFlowReport(os_no, prd_no);
    }

}
