package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.utils.entity.WorkFlowArea;
import com.giants3.hd.utils.entity.WorkFlowEvent;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowEventConfigIPresenter extends IPresenter {

    void addOne();

    void showOne(WorkFlowEvent workFlowWorker);
    void showOne(WorkFlowArea workFlowArea);

    void addOneArea();
}
