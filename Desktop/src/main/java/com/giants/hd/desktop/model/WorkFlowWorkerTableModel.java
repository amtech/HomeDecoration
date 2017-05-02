package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlow_Config;
import com.giants3.hd.utils.entity.WorkFlowArrangeData;
import com.giants3.hd.utils.entity.WorkFlowWorker;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowWorkerTableModel extends  BaseListTableModel<WorkFlowWorker> {




    public WorkFlowWorkerTableModel( ) {
        super(WorkFlowWorker.class, TableStructureUtils.getWorkFlowWorker());

    }






}
