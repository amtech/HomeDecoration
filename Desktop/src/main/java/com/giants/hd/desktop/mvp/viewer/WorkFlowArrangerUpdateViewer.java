package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlowArranger;

import java.util.List;

/**
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowArrangerUpdateViewer extends IViewer {



    void bindData(WorkFlowArranger workFlowWorker);

    void bindUsers(List<User> users);

    void getData(WorkFlowArranger workFlowWorker);
}
