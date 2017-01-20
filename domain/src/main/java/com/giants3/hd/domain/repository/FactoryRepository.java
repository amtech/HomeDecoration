package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import rx.Observable;

import java.util.List;

/**
 *
 * 厂家数据接口
 * Created by david on 2015/10/6.
 */
public interface FactoryRepository {


    Observable<RemoteData<OutFactory>> getOutFactories();

    Observable<RemoteData<OutFactory>> saveOutFactories(List<OutFactory> datas);

    Observable<RemoteData<OrderItemWorkFlow>> startOrderItemWorkFlow(OrderItemWorkFlow orderItemWorkFlow);
}
