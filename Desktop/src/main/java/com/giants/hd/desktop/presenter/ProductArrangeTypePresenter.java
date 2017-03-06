package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.WorkFlowSubType;

import java.util.List;

/**
 * 产品排厂类型
 * 二级流程 铁件 木件  PU，其他
 * Created by davidleen29 on 2017/2/19.
 */
public interface ProductArrangeTypePresenter extends Presenter {
    void saveData(List<WorkFlowSubType> newData);
}
