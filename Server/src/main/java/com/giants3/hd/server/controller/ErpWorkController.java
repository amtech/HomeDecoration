package com.giants3.hd.server.controller;

import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Zhilingdan;
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
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/searchZhiling", method = RequestMethod.GET)
    @ResponseBody
    public RemoteData<Zhilingdan> searchZhilingdan(@RequestParam("osName") String osName,  @RequestParam("startDate") String startDate, @RequestParam("endDate")  String endDate)
    {


        return  erpWorkService.searchZhilingdan(osName,startDate,endDate);
    }

 }
