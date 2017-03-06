package com.giants.hd.desktop.view;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.noEntity.OrderReportItem;

/**
 * 订单报表界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderReportViewer extends AbstractViewer {

    void setData(RemoteData<OrderReportItem> erpOrderRemoteData);
}
