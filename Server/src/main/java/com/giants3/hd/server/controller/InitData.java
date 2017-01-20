package com.giants3.hd.server.controller;

import com.giants3.hd.server.entity.AppVersion;
import com.giants3.hd.server.entity.GlobalData;
import com.giants3.hd.server.entity.Module;
import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.repository.ModuleRepository;
import com.giants3.hd.server.repository.WorkFlowRepository;
import com.giants3.hd.server.service.GlobalDataService;
import com.giants3.hd.server.service.WorkFlowService;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 应用程序启动初始化
 * Created by davidleen29 on 2015/8/7.
 */
@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {


    private static final Logger logger = Logger.getLogger(InitData.class);
    private static boolean isStart = false;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AppVersionRepository appVersionRepository;


    @Autowired
    GlobalDataService globalDataService;

    @Autowired
    WorkFlowRepository workFlowRepository;
    @Autowired
    WorkFlowService workFlowService;
//
//    @Autowired
//    TestXmlRepository testXmlRepository;


    @Autowired
    TaskController taskController;

    @Value("${appfilepath}")
    private String appFilePath;


    @Autowired
    MaterialController materialController;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {


            //logs debug message
            if (logger.isDebugEnabled()) {
                logger.debug("getWelcome is executed!");
            }


            List<Module> moduleList = Module.getInitDataList();
            int newSize = moduleList.size();

            int existSize = moduleRepository.findAll().size();
            //模块数据初始化
            if (existSize != newSize) {

                for (Module module : Module.getInitDataList()) {

                    Module oldModule = moduleRepository.findFirstByNameEquals(module.name);
                    if (oldModule == null) {
                        oldModule = new Module();
                        oldModule.name = module.name;
                        oldModule.title = module.title;
                        moduleRepository.save(module);

                    }


                }

                moduleRepository.flush();

            }


            //版本数据初始化。

            AppVersion appVersion = null;

            File f = new File(appFilePath);
            if (f.isDirectory()) {

                File[] files = f.listFiles();

                for (File aFile : files) {

                    if (aFile.getName().endsWith(".jar")) {
                        String fileName = aFile.getName();
                        //读取相关的版本配置文件
                        Properties properties = readZipFile(aFile.getAbsolutePath(), "version.properties");
                        appVersion = new AppVersion();
                        appVersion.memo = properties.getProperty("Version_Spec");
                        appVersion.versionName = properties.getProperty("Version_Name");
                        String versionCodeString = properties.getProperty("Version_Number");
                        appVersion.updateTime = Calendar.getInstance().getTimeInMillis();
                        appVersion.timeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(appVersion.updateTime));
                        appVersion.versionCode = StringUtils.isEmpty(versionCodeString) ? -1 : Integer.valueOf(versionCodeString);
                        appVersion.appName = fileName;
                        appVersion.fileSize = aFile.length();
                        break;


                    }


                }

            }


            if (appVersion != null) {
                //核对客户端最新版本
                AppVersion oldVersion = appVersionRepository.findFirstByAppNameEqualsOrderByVersionCodeDescUpdateTimeDesc(appVersion.appName);

                if (oldVersion == null) {

                    appVersionRepository.save(appVersion);
                } else if (oldVersion.versionCode < appVersion.versionCode) {
                    //添加记录
                    appVersionRepository.save(appVersion);

                } else if (oldVersion.versionCode == appVersion.versionCode) {
                    //版本一致  文件大小不一致。  使用当前文件大小

                    if (oldVersion.fileSize != appVersion.fileSize) {
                        appVersion.id = oldVersion.id;
                        //修改当前记录
                        appVersionRepository.save(appVersion);

                    }


                }


            }


            //检查全局项目数据 不存在 则构建一份数据（使用默认值）
            GlobalData globalData = globalDataService.findCurrentGlobalData();
            if (globalData == null) {
                globalData = new GlobalData();
                globalDataService.save(globalData);
            }


            workFlowService.initData();
        }


        //定时任务启动

        taskController.resume();


        //开启检查是否有未完成的产品统计数据计算


        materialController.updateProductData(null);


        isStart = true;


        System.out.println("spring 容器初始化完毕================================================");

    }


    public static Properties readZipFile(String zipFilePath, String relativeFilePath) {

        Properties
                properties = new Properties();
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                // if the entry is not directory and matches relative file then extract it
                if (!entry.isDirectory() && entry.getName().equals(relativeFilePath)) {
                    InputStream bis = zipFile.getInputStream(entry);
                    Reader reader = new InputStreamReader(bis, "UTF-8");
                    properties.load(reader);
                    reader.close();
                    bis.close();
                    break;

                } else {
                    continue;
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return properties;
    }


}
