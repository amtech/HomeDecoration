package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
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
    Observable<RemoteData<ErpOrder>> getOrderList(String key, int pageIndex, int pageSize);

    Observable getOrderItemList(String or_no);

    Observable<ErpOrderDetail> getOrderOutDetail(String os_no);

    /**
     * 保存订单详情
     * @param orderDetail
     * @return
     */
    Observable<RemoteData<ErpOrderDetail>>  saveOrderDetail(ErpOrderDetail orderDetail);
}
