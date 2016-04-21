package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.exception.HdException;

import com.giants3.hd.utils.noEntity.ProductDetail;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public class ProductRepositoryImpl extends BaseRepositoryImpl implements ProductRepository {



    @Inject
    ApiManager apiManager;
    @Override
    public Observable<List<Product>> loadByProductNameBetween(final String startName, final String endName, final boolean withCopy) {



        return Observable.create(new Observable.OnSubscribe<List<Product>>() {
            @Override
            public void call(Subscriber<? super List<Product>> subscriber) {




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

    @Override
    public Observable<List<Product>> loadByProductNameRandom(final String productNames, final boolean withCopy) {
        return Observable.create(new Observable.OnSubscribe<List<Product>>() {
            @Override
            public void call(Subscriber<? super List<Product>> subscriber) {




                try {
                    RemoteData<Product> remoteData= apiManager.loadProductListByNameRandom(productNames,withCopy);

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

    @Override
    public Observable<ProductDetail> loadByProductNo(final String prdNo) {
        return Observable.create(new Observable.OnSubscribe<ProductDetail>() {
            @Override
            public void call(Subscriber<? super ProductDetail> subscriber) {




                try {
                    RemoteData<ProductDetail> remoteData= apiManager.loadProductDetailByPrdNo(prdNo);

                    if(remoteData.isSuccess()&&remoteData.datas.size()>0)
                    {
                        subscriber.onNext(remoteData.datas.get(0) );
                        subscriber.onCompleted();

                    }else
                    {
                        if(remoteData.isSuccess())
                        {
                            subscriber.onError(   HdException.create("未找到"+prdNo+" 的产品详细数据"));

                        }else
                        subscriber.onError(   HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }
}
