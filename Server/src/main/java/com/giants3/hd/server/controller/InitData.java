package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.repository.GlobalDataRepository;
import com.giants3.hd.server.repository.ModuleRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.AppVersion;
import com.giants3.hd.utils.entity.GlobalData;
import com.giants3.hd.utils.entity.Module;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 应用程序启动初始化
 * Created by davidleen29 on 2015/8/7.
 */
@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {


    private Log logger = LogFactory.getLog(InitData.class);

    private static boolean isStart = false;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AppVersionRepository appVersionRepository;


    @Autowired
    GlobalDataRepository globalDataRepository;

    @Value("${appfilepath}")
    private String appFilePath;


    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {


      List< Module> moduleList=     Module.getInitDataList();
            int newSize=moduleList.size();

            int existSize=moduleRepository.findAll().size();
            //模块数据初始化
            if(existSize!=newSize)
            {




                for(Module module:Module.getInitDataList())
                {

                    Module oldModule=moduleRepository.findFirstByNameEquals(module.name);
                    if(oldModule==null)
                    {
                        oldModule=new Module();
                        oldModule.name=module.name;
                        oldModule.title=module.title;
                        moduleRepository.save(module);

                    }



                }

                moduleRepository.flush();

            }




            //版本数据初始化。

            AppVersion appVersion=null;

             File f=new File(appFilePath);
            if(f.isDirectory())
            {

                File[] files=f.listFiles();

                for(File aFile : files)
                {

                    if(aFile.getName().endsWith(".jar"))
                    {
                        String fileName=aFile.getName();
                        try {
                            FileInputStream fileInputStream=new FileInputStream(aFile);
                            JarInputStream jarInputStream=new JarInputStream(fileInputStream);
                            Manifest manifest=jarInputStream.getManifest();
                            jarInputStream.close();
                            fileInputStream.close();
                              appVersion=new AppVersion();
                            Attributes attr= manifest.getMainAttributes();
                            appVersion.memo=attr.getValue("Manifest-Version_Spec");
                            appVersion.versionName=attr.getValue("Manifest-Version");
                            String versionCodeString=attr.getValue("Manifest-Version_Number");
                            appVersion.updateTime= Calendar.getInstance().getTimeInMillis();
                            appVersion.versionCode= StringUtils.isEmpty(versionCodeString)?-1:Integer.valueOf(versionCodeString);
                            appVersion.appName=fileName;
                            appVersion.fileSize=aFile.length();
                            break;


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }


            if(appVersion!=null)
            {

                //核对最新版本


                AppVersion oldVersion=appVersionRepository.findFirstByAppNameEqualsOrderByVersionCodeDesc(appVersion.appName);

                if(oldVersion==null)
                {

                    appVersionRepository.save(appVersion);
                }else

                if(oldVersion.versionCode<appVersion.versionCode)
                {
                    appVersionRepository.save(appVersion);

                }




            }






            //检查全局项目数据 不存在 则构建一份数据（使用默认值）
       List<GlobalData> globalDatas=     globalDataRepository.findAll();
        if(globalDatas.size()==0)
        {
            GlobalData globalData=new GlobalData();
            globalDataRepository.save(globalData);
        }






        }





            isStart = true;


        System.out.println("spring 容器初始化完毕================================================");

    }
}
