package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.HdTask;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * Created by david on 2015/12/12.
 */
public class HdTaskRepositoryImpl extends BaseRepositoryImpl implements HdTaskRepository {

    @Inject
    ApiManager apiManager;
    @Override
    public Observable<List<HdTask>> getTaskList() {


        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.loadTaskList();
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });






    }

    @Override
    public Observable<List<HdTask>> addTask(final HdTask task) {

        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.addHdTask(task);
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }

    @Override
    public Observable<List<HdTask>> delete(final long taskId) {
        return Observable.create(new Observable.OnSubscribe<List<HdTask>>() {
            @Override
            public void call(Subscriber<? super List<HdTask>> subscriber) {




                try {
                    RemoteData<HdTask> remoteData= apiManager.deleteHdTask(taskId);
                    handleResult(subscriber,remoteData);

                } catch (HdException e) {
                    subscriber.onError(e);
                }




            }
        });
    }
}
