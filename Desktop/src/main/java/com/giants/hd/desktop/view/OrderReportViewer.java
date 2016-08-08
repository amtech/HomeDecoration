package com.giants.hd.desktop.view;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.StockOutAuth;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity_erp.ErpOrder;

import java.util.List;

/**
 *
 *    订单报表界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderReportViewer extends    AbstractViewer {

    void setData(RemoteData<ErpOrder> erpOrderRemoteData);
}
