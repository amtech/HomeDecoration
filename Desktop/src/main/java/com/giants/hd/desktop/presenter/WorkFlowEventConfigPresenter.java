package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.WorkFlowEvent;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowEventConfigPresenter extends Presenter {

    void addOne();

    void showOne(WorkFlowEvent workFlowWorker);
}
