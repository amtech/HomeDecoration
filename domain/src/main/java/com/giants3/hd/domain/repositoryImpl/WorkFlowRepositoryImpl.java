package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * 生产进度
 * Created by david on  20160917
 */
public class WorkFlowRepositoryImpl extends BaseRepositoryImpl implements WorkFlowRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<WorkFlow>> getWorkFlowList() {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlow>> subscriber) {


                try {
                    RemoteData<WorkFlow> remoteData = apiManager.getWorkFlowList( );
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
    public Observable<RemoteData<WorkFlow>> saveWorkFlowList( final List<WorkFlow> workFlows) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlow>> subscriber) {


                try {
                    RemoteData<WorkFlow> remoteData = apiManager.saveWorkFlowList( workFlows);
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

    /**
     * 启动订单跟踪
     *
     * @param os_no
     * @return
     */
    @Override
    public Observable<RemoteData<Void>> startOrderTrack(final String os_no) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {


                try {
                    RemoteData<Void> remoteData = apiManager.startOrderTrack(os_no);
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
    public Observable<RemoteData<WorkFlowSubType>> getWorkFlowSubTypes() {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowSubType>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowSubType>> subscriber) {


                try {
                    RemoteData<WorkFlowSubType> remoteData = apiManager.getWorkFlowSubTypes( );
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
    public Observable<RemoteData<WorkFlowProduct>> getWorkFlowOfProduct(final long productId) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowProduct>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowProduct>> subscriber) {


                try {
                    RemoteData<WorkFlowProduct> remoteData = apiManager.getWorkFlowOfProduct( productId);
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
    public Observable<RemoteData<WorkFlowProduct>> saveWorkFlowProduct(final WorkFlowProduct workFlowProduct) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowProduct>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowProduct>> subscriber) {


                try {
                    RemoteData<WorkFlowProduct> remoteData = apiManager.saveWorkFlowProduct( workFlowProduct);
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


    /**
     * 获取货款的进度数据
     *
     * @param orderItemId
     * @return
     */
    @Override
    public Observable<RemoteData<OrderItemWorkFlowState>> getOrderItemWorkFlowState(final long orderItemId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderItemWorkFlowState>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderItemWorkFlowState>> subscriber) {


                try {
                    RemoteData<OrderItemWorkFlowState> remoteData = apiManager.getOrderItemWorkFlowState( orderItemId);
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
}

