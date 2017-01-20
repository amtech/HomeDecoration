package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowSubType;

import java.util.List;

/**
 *
 *    生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowViewer extends    AbstractViewer {


    void setData(List<WorkFlow> datas);
    void setSubData(List<WorkFlowSubType> datas);
    void setUserList(List<User> users);
}
