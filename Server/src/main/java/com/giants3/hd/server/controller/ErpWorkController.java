package com.giants3.hd.server.controller;

import com.giants3.hd.server.entity.AppVersion;
import com.giants3.hd.server.entity.Material;
import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.service.ErpWorkService;
import com.giants3.hd.server.service.MaterialService;
import com.giants3.hd.server.service.ProductService;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Zhilingdan;
import de.greenrobot.common.io.IoUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;

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
