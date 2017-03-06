package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.WorkFlowSubType;

import java.util.List;

/**
 *
 * 产品 排厂类型
 * 二级流程 铁件 木件  PU，其他
 *展示层
 * Created by davidleen29 on 2017/1/15.
 */
public interface ProductArrangeTypeViewer extends AbstractViewer {

    void setData(List<WorkFlowSubType> datas);
}
