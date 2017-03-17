package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import com.giants3.hd.utils.entity.Zhilingdan;

import java.util.List;

/**
 *
 *      界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface ZhilingdanViewer extends    AbstractViewer {


    void setData(List<Zhilingdan> datas);


}
