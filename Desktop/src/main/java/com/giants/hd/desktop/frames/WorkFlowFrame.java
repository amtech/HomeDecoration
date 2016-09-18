package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.presenter.WorkFlowPresenter;
import com.giants.hd.desktop.view.WorkFlowViewer;
import com.giants.hd.desktop.viewImpl.Panel_Stock_List;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.google.gson.Gson;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *  生产流程
 *
 * Created by david on 20160303
 */
public class WorkFlowFrame extends BaseInternalFrame implements WorkFlowPresenter {
    WorkFlowViewer workFlowViewer;


    private String oldData;
    private List<WorkFlow> data;
    public WorkFlowFrame( ) {
        super("生产流程配置");




        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                readData();
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadUsers();
            }
        });


    }
    private  void  loadUsers()
    {

        UseCaseFactory.getInstance().createGetUserListUseCase().execute(new Subscriber<java.util.List<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(java.util.List<User> users) {


                workFlowViewer.setUserList(users);

            }

        });

    }


    private void readData()
    {

        UseCaseFactory.getInstance().createGetWorkFlowUseCase().execute(new Subscriber<RemoteData<WorkFlow>>() {
            @Override
            public void onCompleted() {
                workFlowViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlow> workFlowRemoteData) {

                if(workFlowRemoteData.isSuccess())
                {


                    setData(workFlowRemoteData.datas);
                }


            }


        });
        workFlowViewer.showLoadingDialog();
    }


    public void setData(List<WorkFlow> datas)
    {
        oldData= GsonUtils.toJson(datas);
        this.data=datas;
        workFlowViewer.setData(datas);
    }


    @Override
    protected Container getCustomContentPane() {
        workFlowViewer =new Panel_Work_Flow( this);
        return workFlowViewer.getRoot();
    }


    @Override
    public void save( ) {

        if(!hasModifyData())
        {
            workFlowViewer.showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkFlowUseCase(data).execute(new Subscriber<RemoteData<WorkFlow>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlow> workFlowRemoteData) {
                workFlowViewer.hideLoadingDialog();
                if(workFlowRemoteData.isSuccess())
                {
                    setData(workFlowRemoteData.datas);
                    workFlowViewer.showMesssage("保存成功");
                }


            }


        });
        workFlowViewer.showLoadingDialog();
    }

    @Override
    public boolean hasModifyData() {

      return   data!=null && !GsonUtils.toJson(data).equals(oldData);
    }
}
