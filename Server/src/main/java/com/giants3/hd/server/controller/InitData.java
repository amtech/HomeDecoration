package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.ModuleRepository;
import com.giants3.hd.utils.entity.Module;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

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




    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {



            if(moduleRepository.findAll().size()==0)
            {
                //无数据添加
                for(Module module:Module.getInitDataList())
                {
                    moduleRepository.save(module);

                }

                moduleRepository.flush();

            }





        }
            isStart = true;
        logger.info( "Start to load CMS Meta data");



        logger.info ("End to load CMS Meta data");

        logger.info( "Start to load Other data");

        System.out.println("spring 容器初始化完毕================================================");

    }
}
