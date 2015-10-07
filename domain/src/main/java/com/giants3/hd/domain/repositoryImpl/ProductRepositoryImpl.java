package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.exception.HdException;

import com.google.inject.Guice;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public Observable<List<Product>> loadByProductNameBetween(final String startName, final String endName, final boolean withCopy) {



        return Observable.create(new Observable.OnSubscribe<List<Product>>() {
            @Override
            public void call(Subscriber<? super List<Product>> subscriber) {



                ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
                try {
                    RemoteData<Product> remoteData= apiManager.loadProductListByNameBetween(startName, endName,withCopy);

                    if(remoteData.isSuccess())
                    {
                        subscriber.onNext(remoteData.datas );
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
