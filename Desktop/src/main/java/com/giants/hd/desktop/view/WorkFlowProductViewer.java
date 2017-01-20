package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;

import java.util.List;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowProductViewer extends AbstractViewer {


    void setData(WorkFlowProduct workFlowProduct);

    /**
     * 获取用户编辑结果
     * @param data
     */
   void getData(WorkFlowProduct  data);
}
