package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;

import java.io.File;

/**
 *
 * 订单报表展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderReportPresenter extends    Presenter {


    void search(String key, String dateStart,String dateEnd, int pageIndex, int pageSize);

    void loadOrderDetail(ErpOrder erpOrder);
}
