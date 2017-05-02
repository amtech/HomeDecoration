package com.giants.hd.desktop.view;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.ErpOrderItem;

/**
 * Created by davidleen29 on 2017/4/19.
 */
public interface OrderItemForArrangeListViewer extends  AbstractViewer {
    void setData(RemoteData<ErpOrderItem> orderReportItemRemoteData);
}
