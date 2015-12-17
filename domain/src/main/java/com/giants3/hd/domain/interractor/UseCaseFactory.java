package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.domain.repositoryImpl.HdTaskRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.XiankangRepositoryImpl;
import com.giants3.hd.utils.entity.HdTask;
import com.google.inject.Guice;
import rx.schedulers.Schedulers;


/**
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory  {








    public static UseCase createQuotationDetail(long qutationId )
    {


        return new GetQuotationDetail( Schedulers.newThread()    ,Schedulers.immediate(),qutationId, new QuotationRepositoryImpl());
    }


    public static UseCase createProductByNameBetween(String startName,String endName,boolean withCopy)
    {




        return new ProductUseCase( Schedulers.newThread()    ,Schedulers.immediate(),startName,endName,withCopy, new ProductRepositoryImpl());
    }


    public static UseCase createUpdateXiankang()
    {

        return new UpdateXiankangUseCase(Schedulers.newThread(),Schedulers.immediate(),new XiankangRepositoryImpl());
    }



    public static UseCase readTaskListUseCase()
    {

        return new HdTaskListUseCase(Schedulers.newThread(),Schedulers.immediate(),new HdTaskRepositoryImpl());
    }

    public static UseCase addHdTaskUseCase(HdTask hdTask)
    {

        return new HdTaskAddUseCase(Schedulers.newThread(),Schedulers.immediate(),hdTask,new HdTaskRepositoryImpl());
    }


    public static UseCase deleteHdTaskUseCase(long taskId)
    {
        Guice.createInjector().getInstance(HdTaskRepositoryImpl.class);

        return new HdTaskDeleteUseCase(Schedulers.newThread(),Schedulers.immediate(),  taskId,new HdTaskRepositoryImpl());
    }
}
