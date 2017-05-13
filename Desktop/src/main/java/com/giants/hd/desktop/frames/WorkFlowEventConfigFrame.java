package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowArrangerUpdateDialog;
import com.giants.hd.desktop.presenter.WorkFlowEventConfigPresenter;
import com.giants.hd.desktop.view.WorkFlowEventConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Event_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ModuleConstant;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlowArranger;
import com.giants3.hd.utils.entity.WorkFlowEvent;
import rx.Subscriber;

import javax.swing.*;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowEventConfigFrame extends BaseMVPFrame<WorkFlowEventConfigViewer> implements WorkFlowEventConfigPresenter {


    public WorkFlowEventConfigFrame() {

        super(ModuleConstant.TITLE_WORK_FLOW_ITEM_EVENT_CONFIG);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetWorkFlowEventListUseCase().execute(new Subscriber<RemoteData<WorkFlowEvent>>() {
            //            @Override
            public void onCompleted() {
                getViewer().hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewer().hideLoadingDialog();
                getViewer().showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowEvent> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    setData(workFlowRemoteData.datas);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowEvent> datas) {


        getViewer().bindData(datas);
    }


    @Override
    protected WorkFlowEventConfigViewer createViewer() {
        return new Panel_Work_Flow_Event_List(this);
    }

    @Override
    public void addOne() {


        WorkFlowArrangerUpdateDialog dialog = new WorkFlowArrangerUpdateDialog(SwingUtilities.getWindowAncestor(this), null);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowArranger workFlowWorker = dialog.getResult();
        if (workFlowWorker != null) {
            readData();
        }


    }

    @Override
    public void showOne(WorkFlowEvent workFLowEvent) {
//        WorkFlowArrangerUpdateDialog dialog = new WorkFlowArrangerUpdateDialog(SwingUtilities.getWindowAncestor(this), workFLowEvent);
//        dialog.setLocationByPlatform(true);
//        dialog.setVisible(true);
//        WorkFlowArranger result = dialog.getResult();
//        if (result != null) {
//            readData();
//        }
    }
}
