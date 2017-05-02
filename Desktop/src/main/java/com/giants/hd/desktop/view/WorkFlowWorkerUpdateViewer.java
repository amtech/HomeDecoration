package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowWorker;

import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowWorkerUpdateViewer extends AbstractViewer {


    void bindWorkFlows(List<WorkFlow> workFlows);

    void bindUsers(List<User> users);

    void bindData(WorkFlowWorker workFlowWorker);

    void getData(WorkFlowWorker workFlowWorker);
}
