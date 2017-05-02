package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.WorkFlowArrangerUpdateDialog;
import com.giants.hd.desktop.dialogs.WorkFlowWorkerUpdateDialog;
import com.giants.hd.desktop.presenter.WorkFlowArrangerPresenter;
import com.giants.hd.desktop.presenter.WorkFlowWorkerPresenter;
import com.giants.hd.desktop.view.WorkFlowArrangerViewer;
import com.giants.hd.desktop.view.WorkFlowWorkerViewer;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Arranger;
import com.giants.hd.desktop.viewImpl.Panel_Work_Flow_Worker;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ModuleConstant;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlowArranger;
import com.giants3.hd.utils.entity.WorkFlowWorker;
import rx.Subscriber;

import javax.swing.*;
import java.util.List;

/**
 * 流程工作人配置表
 * Created by david on 2015/11/23.
 */
public class WorkFlowArrangerFrame extends BaseMVPFrame<WorkFlowArrangerViewer> implements WorkFlowArrangerPresenter {


    public WorkFlowArrangerFrame() {


        super(ModuleConstant.TITLE_WORK_FLOW_ARRANGER);


        readData();

    }

    private void readData() {


        UseCaseFactory.getInstance().createGetWorkFlowArrangerUseCase().execute(new Subscriber<RemoteData<WorkFlowArranger>>() {
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
            public void onNext(RemoteData<WorkFlowArranger> workFlowRemoteData) {
                getViewer().hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {


                    setData(workFlowRemoteData.datas);

                } else

                    getViewer().showMesssage(workFlowRemoteData.message);


            }


        });
        getViewer().showLoadingDialog();
    }

    private void setData(List<WorkFlowArranger> datas) {


        getViewer().bindData(datas);
    }


    @Override
    protected WorkFlowArrangerViewer createViewer() {
        return new Panel_Work_Flow_Arranger(this);
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
    public void showOne(WorkFlowArranger workFlowWorker) {
        WorkFlowArrangerUpdateDialog dialog = new WorkFlowArrangerUpdateDialog(SwingUtilities.getWindowAncestor(this), workFlowWorker);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        WorkFlowArranger result = dialog.getResult();
        if (result != null) {
            readData();
        }
    }
}
