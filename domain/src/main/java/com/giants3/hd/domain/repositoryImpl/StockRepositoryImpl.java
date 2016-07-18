package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

/**
 * 库存处理
 * Created by david on 2015/10/13.
 */
public class StockRepositoryImpl extends BaseRepositoryImpl implements StockRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<ErpStockOut>> getStockOutList(final String key, final int pageIndex, final int pageSize) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpStockOut>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpStockOut>> subscriber) {


                try {
                    RemoteData<ErpStockOut> remoteData = apiManager.getStockOutList(key, pageIndex, pageSize);
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
    public Observable<RemoteData<ErpStockOutDetail>> getStockOutDetail(final String ck_no) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpStockOutDetail>> subscriber) {
                try {
                    RemoteData<ErpStockOutDetail> remoteData = apiManager.getStockOutDetail(ck_no);
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
