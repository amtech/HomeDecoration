package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.OrderReportItem;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class OrderRepositoryImpl  extends  BaseRepositoryImpl implements OrderRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<ErpOrder>> getOrderList(final String key, final long salesId, final int pageIndex, final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpOrder>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpOrder>> subscriber) {




                try {
                    RemoteData<ErpOrder> remoteData= apiManager.getOrderList(key,salesId,pageIndex,pageSize);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData );
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable getOrderItemList(final String or_no) {
        return Observable.create(new Observable.OnSubscribe<List<ErpOrderItem>>() {
            @Override
            public void call(Subscriber<? super List<ErpOrderItem>> subscriber) {




                try {
                    RemoteData<ErpOrderItem> remoteData= apiManager.getOrderItemList(or_no);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData.datas
                        );
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }


    @Override
    public Observable<ErpOrderDetail> getOrderOutDetail(final String os_no) {
        return Observable.create(new Observable.OnSubscribe<ErpOrderDetail>() {
            @Override
            public void call(Subscriber<? super ErpOrderDetail> subscriber) {




                try {
                    RemoteData<ErpOrderDetail> remoteData= apiManager.getOrderDetail(os_no);
                    if(remoteData.isSuccess())
                    {  subscriber.onNext(remoteData.datas.get(0) );
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }


    @Override
    public Observable<RemoteData<ErpOrderDetail>> saveOrderDetail(final ErpOrderDetail orderDetail) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpOrderDetail>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpOrderDetail>> subscriber) {
                try {
                    RemoteData<ErpOrderDetail> remoteData = apiManager.saveOrderDetail(orderDetail);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    @Override
    public Observable<RemoteData<OrderReportItem>> getOrderReport(final long userId, final String dateStart, final String dateEnd, final int pageIndex, final int pageSize) {


        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderReportItem>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderReportItem>> subscriber) {




                try {
                    RemoteData<OrderReportItem> remoteData= apiManager.getOrderReport(    userId,   dateStart,   dateEnd,   pageIndex,   pageSize);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });

    }

    /**
     * 获取未出库订单货款
     *
     * @return
     */
    @Override
    public Observable<RemoteData<OrderItemWorkFlowState>> getUnCompleteOrderWorkFlowReport() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderItemWorkFlowState>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderItemWorkFlowState>> subscriber) {




                try {
                    RemoteData<OrderItemWorkFlowState> remoteData= apiManager.getUnCompleteOrderWorkFlowReport(   );
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<RemoteData<ErpOrderItem>> getOrderWorkFlowReport(final String key, final int pageIndex, final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpOrderItem>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpOrderItem>> subscriber) {




                try {
                    RemoteData<ErpOrderItem> remoteData= apiManager.getOrderWorkFlowReport(  key,   pageIndex,   pageSize);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<RemoteData<OrderItemWorkFlow>> getOrderItemWorkFlow(final long orderItemId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderItemWorkFlow>> subscriber) {




                try {
                    RemoteData<OrderItemWorkFlow> remoteData= apiManager.getOrderItemWorkFlow(orderItemId);
                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    }else
                    {
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }
}
