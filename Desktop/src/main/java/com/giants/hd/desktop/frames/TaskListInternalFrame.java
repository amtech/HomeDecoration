package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.viewImpl.Panel_Tasks;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.entity.HdTask;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 *
 * 任务管理面板
 * Created by david on 2015/11/23.
 */
public class TaskListInternalFrame extends BaseInternalFrame {


    public TaskListInternalFrame( ) {
        super("定时任务列表");


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadHdTask();

            }
        });


    }

    Panel_Tasks panel_tasks;

    @Override
    protected Container getCustomContentPane() {

        panel_tasks=new Panel_Tasks(this);
        return panel_tasks.getRoot();

    }

    public  void loadHdTask()
    {


        UseCaseFactory.readTaskListUseCase( ).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.setData(tasks);


            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }

    public void addHdTask(HdTask task) {


        UseCaseFactory.addHdTaskUseCase(task).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage("任务添加成功");
                panel_tasks.setData(tasks);


            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();

    }


    /**
     * 删除任务
     * @param id
     */
    public void deleteHdTask(long id) {


        UseCaseFactory.deleteHdTaskUseCase(id).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();

                panel_tasks.showMesssage("任务删除成功");
                panel_tasks.setData(tasks);



            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }
}
