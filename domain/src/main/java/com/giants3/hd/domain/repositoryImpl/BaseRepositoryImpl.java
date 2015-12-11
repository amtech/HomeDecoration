package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.HdTask;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/12/12.
 */
public class BaseRepositoryImpl  {


    public BaseRepositoryImpl()
    {

        Guice.createInjector().injectMembers(this);
    }


    /**
     * 统一处理结果
     * @param subscriber
     * @param remoteData
     * @param <T>
     * @return
     */
    public static <T> void handleResult(Subscriber<? super List<T>> subscriber,RemoteData<T> remoteData)
    {




            if(remoteData.isSuccess())
            {
                subscriber.onNext(remoteData.datas );
                subscriber.onCompleted();

            }else
            {
                subscriber.onError(   HdException.create(remoteData.message));

            }



    }
}
