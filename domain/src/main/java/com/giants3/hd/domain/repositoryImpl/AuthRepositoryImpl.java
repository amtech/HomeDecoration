package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OrderAuth;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.StockOutAuth;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**权限接口
 * Created by david on 2015/10/6.
 */
public class AuthRepositoryImpl extends  BaseRepositoryImpl implements AuthRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<QuoteAuth>> getQuoteAuthList( ) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<QuoteAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<QuoteAuth>> subscriber) {




                try {
                    RemoteData<QuoteAuth> remoteData= apiManager.readQuoteAuth();
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
    public Observable<RemoteData<QuoteAuth>> saveQuoteAuthList(final List<QuoteAuth> quoteAuths) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<QuoteAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<QuoteAuth>> subscriber) {
                try {
                    RemoteData<QuoteAuth> remoteData = apiManager.saveQuoteAuthList(quoteAuths);
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
    public Observable<RemoteData<OrderAuth>> getOrderAuthList() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderAuth>> subscriber) {




                try {
                    RemoteData<OrderAuth> remoteData= apiManager.readOrderAuth();
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
    public Observable<RemoteData<OrderAuth>> saveOrderAuthList(final List<OrderAuth> orderAuths) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<OrderAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<OrderAuth>> subscriber) {
                try {
                    RemoteData<OrderAuth> remoteData = apiManager.saveOrderAuth(orderAuths);
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
    public Observable<RemoteData<StockOutAuth>> getStockOutAuthList() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockOutAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockOutAuth>> subscriber) {




                try {
                    RemoteData<StockOutAuth> remoteData= apiManager.readStockOutAuth();
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
    public Observable<RemoteData<StockOutAuth>> saveStockOutAuthList(final List<StockOutAuth> stockOutAuths) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<StockOutAuth>>() {
            @Override
            public void call(Subscriber<? super RemoteData<StockOutAuth>> subscriber) {
                try {
                    RemoteData<StockOutAuth> remoteData = apiManager.saveStockOutAuth(stockOutAuths);
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
