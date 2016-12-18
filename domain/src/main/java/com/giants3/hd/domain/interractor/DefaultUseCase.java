package com.giants3.hd.domain.interractor;

import com.giants3.hd.utils.RemoteData;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 上传临时文件用例
 * Created by david on 2015/10/7.
 */
public class DefaultUseCase extends UseCase {


    public DefaultUseCase() {
        super(Schedulers.newThread(), Schedulers.immediate());

    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<String>>() {
            @Override
            public void call(Subscriber<? super RemoteData<String>> subscriber) {

                subscriber.onError(new Exception("this is default case"));
            }
        });
    }
}
