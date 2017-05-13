package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.WorkFlowEvent;
import com.giants3.hd.utils.entity.WorkFlowWorker;

import java.util.List;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowEventConfigViewer extends AbstractViewer {


    void bindData(List<WorkFlowEvent> datas);
}
