package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.module.HdTaskModule;
import com.giants3.hd.domain.module.OrderModule;
import com.giants3.hd.domain.module.ProductModule;
import com.giants3.hd.domain.module.QuotationModule;
import com.giants3.hd.domain.repository.HdTaskLogRepository;
import com.giants3.hd.domain.repository.HdTaskRepository;
import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repositoryImpl.HdTaskRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.XiankangRepositoryImpl;
import com.giants3.hd.utils.entity.HdTask;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.schedulers.Schedulers;

import java.sql.Statement;
import java.util.concurrent.ForkJoinPool;


/**   用例工厂类
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory  {




    @Inject
    HdTaskRepository  taskRepository;

    @Inject
    HdTaskLogRepository taskLogRepository;

    @Inject
    OrderRepository orderRepository;
    @Inject
    ProductRepository productRepository;

    private    UseCaseFactory()
    {


        Guice.createInjector(new HdTaskModule(),new QuotationModule(),new OrderModule(),new ProductModule()).injectMembers(this);

    }


    public static   UseCaseFactory factory=null;


    public synchronized static UseCaseFactory getInstance() {



        if (factory == null) {

            factory = new UseCaseFactory();

        }
        return factory;
    }



    public   UseCase createQuotationDetail(long qutationId )
    {


        return new GetQuotationDetail( Schedulers.newThread()    ,Schedulers.immediate(),qutationId, new QuotationRepositoryImpl());
    }


    public   UseCase createProductByNameBetween(String startName,String endName,boolean withCopy)
    {




        return new ProductUseCase( Schedulers.newThread()    ,Schedulers.immediate(),startName,endName,withCopy,productRepository);
    }


    public   UseCase createUpdateXiankang()
    {

        return new UpdateXiankangUseCase(Schedulers.newThread(),Schedulers.immediate(),new XiankangRepositoryImpl());
    }



    public   UseCase readTaskListUseCase()
    {

        return new HdTaskListUseCase(Schedulers.newThread(),Schedulers.immediate(),taskRepository);
    }

    public   UseCase addHdTaskUseCase(HdTask hdTask)
    {

        return new HdTaskAddUseCase(Schedulers.newThread(),Schedulers.immediate(),hdTask,taskRepository);
    }


    public   UseCase deleteHdTaskUseCase(long taskId)
    {


        return new HdTaskDeleteUseCase(Schedulers.newThread(),Schedulers.immediate(),  taskId,taskRepository);
    }

    public   UseCase findTaskLogUseCase(long taskId) {

        return new HdTaskLogListUseCase(Schedulers.newThread(),Schedulers.immediate(),  taskId,taskLogRepository);

    }

    public UseCase createProductByNameRandom(String productList) {

        return new ProductRandomUseCase( Schedulers.newThread()    ,Schedulers.immediate(),productList, productRepository);
    }



    public UseCase createOrderListUseCase(String key,int pageIndex,int pageSize) {

        return new GetOrderListUseCase( Schedulers.newThread()    ,Schedulers.immediate(),key,pageIndex,pageSize, orderRepository);
    }

    public UseCase createOrderItemListUseCase(String os_no) {


        return new GetOrderItemListUseCase( Schedulers.newThread()    ,Schedulers.immediate(),os_no, orderRepository);
    }

    public UseCase createGetProductByPrdNo(String prdNo) {
        return new GetProductByPrdNoUseCase(Schedulers.newThread()    ,Schedulers.immediate(),prdNo, productRepository);
    }
}
