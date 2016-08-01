package com.giants.hd.desktop.view;

import com.giants3.hd.utils.noEntity.ErpOrderDetail;

import java.util.List;

/**
 * 订单详情 界面展示接口
 * Created by davidleen29 on 2016/7/26.
 */
public interface OrderDetailViewer extends  AbstractViewer {

    /**
     * 显示订单数据
     * @param orderDetail
     */
    void setOrderDetail(ErpOrderDetail orderDetail);

    void setEditable(boolean b);

    void showAttachFiles(List<String> attachStrings);
}
