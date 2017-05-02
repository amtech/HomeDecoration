package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowArranger;
import com.giants3.hd.utils.entity.WorkFlowWorker;

import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowArrangerUpdateViewer extends AbstractViewer {



    void bindData(WorkFlowArranger workFlowWorker);

    void bindUsers(List<User> users);

    void getData(WorkFlowArranger workFlowWorker);
}
