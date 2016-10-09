package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.OrderReportItem;
import rx.Observable;

/**
 * Created by david on 2015/10/6.
 */
public interface OrderRepository {


    /**
     * Get an {@link Observable} which will emit a List of {@link com.giants3.hd.utils.entity_erp.ErpOrder}.
     * <p/>
     * 获取产品信息 根据 产品名称顺序取值
     */
    Observable<RemoteData<ErpOrder>> getOrderList(String key,long salesId, int pageIndex, int pageSize);

    Observable getOrderItemList(String or_no);

    Observable<ErpOrderDetail> getOrderOutDetail(String os_no);

    /**
     * 保存订单详情
     * @param orderDetail
     * @return
     */
    Observable<RemoteData<ErpOrderDetail>>  saveOrderDetail(ErpOrderDetail orderDetail);

    /**
     * 订单报表查询    验货日期
     * @param   userId
     * @param dateStart
     * @param dateEnd
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<RemoteData<OrderReportItem>>  getOrderReport(long userId, String dateStart, String dateEnd, int pageIndex, int pageSize);

    /**
     * 获取未出库订单货款
     * @return
     */
    Observable<RemoteData<ErpOrderItem>> getUnCompleteOrderWorkFlowReport();

    Observable<RemoteData<ErpOrderItem>> getOrderWorkFlowReport(String key, int pageIndex, int pageSize);
}
