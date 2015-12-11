package com.giants3.hd.server.controller;

import com.giants3.hd.server.entity.HdTimerTask;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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


    private Timer timer=new Timer();


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
        if(hdTask.date<=current)
        {
            return wrapError("提交的任务运行时刻不能比当前时小");
        }
        hdTask.activator=user.code+","+user.name+","+user.chineseName;
        hdTask.activateTime= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date());



        //添加新任务

        hdTask=    taskRepository.save(hdTask);

        HdTimerTask timerTask=new HdTimerTask(hdTask);
        timer.schedule(timerTask,new Date(hdTask.date));
        timerTasks.add(timerTask);


        return list();


    }
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<HdTask> list( )   {
        List<HdTask> currentTasks=new ArrayList<>() ;
        for(HdTimerTask timerTask:timerTasks)
        {
            currentTasks.add(timerTask.hdTask);
        }
        return  wrapData(currentTasks);
    }
    @RequestMapping(value="/delete", method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<HdTask> delete(@RequestParam(value = "id", required = true) long taskId )   {


        HdTimerTask deleteTask=null;
        for(HdTimerTask timerTask:timerTasks)
        {
            if(timerTask.hdTask.id==taskId)
                deleteTask=timerTask;

        }
        if(deleteTask==null)
        {

            return wrapError("未找到要删除的任务");
        }


        taskRepository.delete(taskId);


        deleteTask.cancel();
        timerTasks.remove(deleteTask);






        return list();
    }

    /**
     * application 重启后  自动恢复未执行任务
     */
    public void resume( )   {



        long current= Calendar.getInstance().getTimeInMillis();



        List<HdTask> tasks=taskRepository.findByDateGreaterThan(current);

        for (HdTask hdTask :
                tasks) {


            HdTimerTask hdTimerTask=new HdTimerTask(hdTask);
            timerTasks.add(hdTimerTask);
            timer.schedule(hdTimerTask,new Date(hdTask.date));

        }



    }




}