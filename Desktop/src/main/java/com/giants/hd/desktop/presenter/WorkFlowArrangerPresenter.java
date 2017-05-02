package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.WorkFlowArranger;
import com.giants3.hd.utils.entity.WorkFlowWorker;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowArrangerPresenter extends Presenter {
    void addOne();

    void showOne(WorkFlowArranger workFlowWorker);
}
