package com.giants.hd.desktop.view;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.ErpOrder;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.utils.noEntity.OrderReportItem;

/**
 *
 *    订单生产流程报表界面层接口
 * Created by davidleen29 on 2016/9/25.
 */
public interface OrderWorkFlowReportViewer extends    AbstractViewer {

    void setData(RemoteData<ErpOrderItem> erpOrderRemoteData);

    void setUnCompleteData(RemoteData<ErpOrderItem> remoteData);
}
