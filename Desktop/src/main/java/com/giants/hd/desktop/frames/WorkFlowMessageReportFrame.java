package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowAreaUpdateDialog;
import com.giants.hd.desktop.dialogs.WorkFlowArrangerUpdateDialog;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.WorkFlowMessageReportPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Event_List;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Message_Report;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.WorkFlowArea;
import com.giants3.hd.entity.WorkFlowArranger;
import com.giants3.hd.entity.WorkFlowEvent;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;

import javax.swing.*;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowMessageReportFrame extends BaseMVPFrame<WorkFlowMessageReportViewer> implements WorkFlowMessageReportPresenter {


    public WorkFlowMessageReportFrame() {

        super(ModuleConstant.TITLE_UNHANDLE_MESSAGE_REPORT);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetUnHandleWorkFlowMessageReportUseCase(24).execute(new RemoteDataSubscriber<WorkFlowMessage>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<WorkFlowMessage> data) {
                setData(data.datas);
            }
        });


        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowMessage> datas) {


        getViewer().bindData(datas);
    }



    @Override
    protected WorkFlowMessageReportViewer createViewer() {
        return new Panel_Work_Flow_Message_Report(this);
    }





}
