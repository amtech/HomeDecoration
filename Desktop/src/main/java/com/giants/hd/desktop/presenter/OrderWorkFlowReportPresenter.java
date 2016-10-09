package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity_erp.ErpOrder;

/**
 *
 * 订单报表展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderWorkFlowReportPresenter extends    Presenter {


    /**
     * 查询未出库货款
     */
    void searchUnDoneOrder( );



    /**
     * 导出报表
     */
    void export();

    void search(String key, int pageIndex, int pageSize);
}
