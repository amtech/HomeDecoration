package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.UserRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class OrderRepositoryImpl implements OrderRepository {


    @Override
    public Observable<RemoteData<ErpOrder>> getOrderList(final String key, final int pageIndex, final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpOrder>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpOrder>> subscriber) {



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<ErpOrder> remoteData= apiManager.getOrderList(key,pageIndex,pageSize);
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



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
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
}
