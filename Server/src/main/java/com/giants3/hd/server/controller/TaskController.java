package com.giants3.hd.server.controller;

import com.giants3.hd.server.service.HdTimerTask;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * 应用程序相关控制类
* Created by davidleen29 on 2014/9/18.
*/
@Controller

@RequestMapping("/task")
public class TaskController extends  BaseController {




    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MaterialController materialController;
    @Autowired
    private TaskLogRepository taskLogRepository;


    private Timer timer=new Timer();

    @Resource
    private PlatformTransactionManager transactionManager;


    private List<HdTimerTask> timerTasks=new ArrayList<>();

    private List<HdTimerTask> removedTimerTask=new ArrayList<>();


    /**
     * 定时任务
     * @return
     */
    @RequestMapping(value = "/schedule", method = {RequestMethod.POST,RequestMethod.GET})
    @Transactional
    public
    @ResponseBody
    RemoteData<HdTask> scheduleTask(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody HdTask hdTask) {


        long current= Calendar.getInstance().getTimeInMillis();

        //hdTask 数据检验
        if(hdTask.startDate <=current)
        {
            return wrapError("提交的任务运行时刻不能比当前时小");
        }
        hdTask.activator=user.code+","+user.name+","+user.chineseName;
        hdTask.activateTime= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(current));

        //添加新任务
        postNewTask(hdTask);



        return list(0,100);


    }
    @RequestMapping(value="/listLog", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> listLog(@RequestParam(value = "taskId", required = true ) long taskId, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<HdTask> pageValue = taskLogRepository.findByTaskIdEqualsOrderByExecuteTimeDesc(taskId  ,pageable );
        List<HdTask> haTasks = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), haTasks);



    }



        @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> list( @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize)   {



        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<HdTask> pageValue = taskRepository.findByTaskNameLikeOrderByStartDateDesc("%%"  ,pageable );

        List<HdTask> haTasks = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), haTasks);

    }
    @RequestMapping(value="/delete", method = {RequestMethod.GET,RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData<HdTask> delete(@RequestParam(value = "id", required = true) long taskId )   {


        HdTimerTask deleteTask=null;
        for(HdTimerTask timerTask:timerTasks)
        {
            if(timerTask.hdTask.id==taskId)
                deleteTask=timerTask;

        }
        if(null!=deleteTask) {
            deleteTask.cancel();
            timerTasks.remove(deleteTask);
        }
        taskRepository.delete(taskId);










        return list(0,100);
    }

    /**
     * application 重启后  自动恢复未执行任务
     */
    public void resume( )   {



        long current= Calendar.getInstance().getTimeInMillis();



        List<HdTask> tasks=taskRepository.findAll( );

        for (HdTask hdTask :                tasks) {

            if(hdTask.repeatCount>hdTask.executeCount)
            {

                long startTime=hdTask.startDate;
                long timeADay= 24l * 60 * 60 * 1000;
                //保证启动时间在今天之后
                if(startTime<current) {
                    while (startTime < current) {
                        startTime +=timeADay;
                    }

                }

                HdTimerTask hdTimerTask=new HdTimerTask(hdTask,transactionManager,materialController, taskLogRepository, timerTasks,taskRepository, timer);
                timerTasks.add(hdTimerTask);
                timer.schedule(hdTimerTask,new Date(startTime));
            }



        }



    }


    /**
     * 启动新任务
     * @param hdTask
     */
    public  void postNewTask(HdTask hdTask)
    {

        //添加新任务
        hdTask=    taskRepository.save(hdTask);
        HdTimerTask timerTask=new HdTimerTask(hdTask,transactionManager,materialController, taskLogRepository, timerTasks,taskRepository, timer);
        timer.schedule(timerTask,new Date(hdTask.startDate));
        timerTasks.add(timerTask);

    }




}