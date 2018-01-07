package com.giants3.hd.server.service;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.server.interf.Job;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.server.repository.TaskLogRepository;
import com.giants3.hd.server.repository.TaskRepository;
import com.giants3.hd.utils.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by davidleen29 on 2016/8/15.
 */
@Service
public class ScheduleService extends AbstractService {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ProductService productService;

    @Autowired ErpService erpService;

    @Autowired ErpWorkService erpWorkService;

    @Autowired
    TaskLogRepository taskLogRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    Job taskJob;

    /**
     * 更新附件路径  定时将附件
     */
    // @Scheduled(fixedDelay = 5000)
    public void  test() {
        System.out.println("processing next 10 at " + new Date());
        for (int i = 0; i < 10; i++) {
            taskJob.doTask(counter.incrementAndGet());
        }

    }

    /**
     * 每天4点触发 更新附件图片
     */
    //@Scheduled(fixedDelay = 50000)
   @Scheduled(cron =  "0 0 4 * * ?" )
    public void updateAttaches() {
//        System.out.println("processing next 10 at " + new Date()+ Thread.currentThread().getName());
//        for (int i = 0; i < 10; i++) {
//            taskJob.doTask(counter.incrementAndGet());
//        }


     HdTask task=   taskRepository.findFirstByTaskNameEquals(HdTask.NAME_UPDATE_ATTACH);
        if(task==null)
        {
            task=new HdTask();
            task.activateTime="每天凌晨四点";
            task.activator="系统";
            task.repeatCount=0;
            task.memo="附件上传在临时文件夹，定时整理数据";
            task.startDate=System.currentTimeMillis();
            task.dateString=DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(task.startDate));
            task.taskName=HdTask.NAME_UPDATE_ATTACH;
            task.taskType=HdTask.TYPE_UPDATE_ATTACH;
            task.executeCount=1;
            task=taskRepository.save(task);

        }else
        {
            task.executeCount++;
            task=taskRepository.save(task);
        }


        HdTaskLog taskLog=new HdTaskLog();
        taskLog.taskTypeName=task.taskName;
        taskLog.taskId=task.id;
        taskLog.taskTypeName=task.taskName;
        taskLog.state=HdTaskLog.STATE_SUCCESS;
        taskLog.stateName="执行成功！";
        long time= System.currentTimeMillis();
        taskLog.executeTime=time;
        taskLog.executeTimeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(time));
        try {
            productService.updateAttachFiles();
            erpService.updateAttachFiles();
        }catch (Throwable t)
        {
            taskLog.stateName="执行失败！";
            taskLog.state=HdTaskLog.STATE_FAIL;
            taskLog.errorMessage=t.getMessage();
        }

        //计算耗时， 以秒为单位
        taskLog.timeSpend=(System.currentTimeMillis()-time)/1000;
        taskLogRepository.save(taskLog);



    }

//    /**
//     * 定时一分钟触发一次
//     */
//    @Scheduled(fixedDelay = 60000)
//    public void test1() {
//      //  updateAttaches();
//
//    }

    /**
     * 每日4点执行 未完成货款的进度数据的更新
     */
    @Scheduled(cron =  "0 0 0 * * ?" )
    public void updateOrderItemWorkFlowState() {

        HdTask task=   taskRepository.findFirstByTaskNameEquals(HdTask.NAME_UPDATE_WORK_FLOW_STATE);
        if(task==null)
        {
            task=new HdTask();
            task.activateTime="每天凌晨四点";
            task.activator="系统";
            task.repeatCount=0;
            task.memo="定时更新在产货款的进度状态";
            task.startDate=System.currentTimeMillis();
            task.dateString=DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(task.startDate));
            task.taskName=HdTask.NAME_UPDATE_WORK_FLOW_STATE;
            task.taskType=HdTask.TYPE_UPDATE_WORK_FLOW_STATE;
            task.executeCount=1;
            task=taskRepository.save(task);

        }else
        {
            task.executeCount++;
            task=taskRepository.save(task);
        }




        HdTaskLog taskLog=new HdTaskLog();
        taskLog.taskTypeName=task.taskName;
        taskLog.taskId=task.id;
        taskLog.taskTypeName=task.taskName;
        taskLog.state=HdTaskLog.STATE_SUCCESS;
        taskLog.stateName="执行成功！";
        long time= System.currentTimeMillis();
        taskLog.executeTime=time;
        taskLog.executeTimeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(time));
        try {
            erpWorkService.updateAllProducingWorkFlowReports();

        }catch (Throwable t)
        {
            taskLog.stateName="执行失败！";
            taskLog.state=HdTaskLog.STATE_FAIL;
            taskLog.errorMessage=t.getMessage();
        }

        //计算耗时， 以秒为单位
        taskLog.timeSpend=(System.currentTimeMillis()-time)/1000;
        taskLogRepository.save(taskLog);


    }



}
