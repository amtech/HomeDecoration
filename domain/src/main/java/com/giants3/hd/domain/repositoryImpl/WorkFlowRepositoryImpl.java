package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
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
}

