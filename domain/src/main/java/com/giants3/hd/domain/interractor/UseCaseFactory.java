package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.module.*;
import com.giants3.hd.domain.repository.*;
import com.giants3.hd.utils.entity.HdTask;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.schedulers.Schedulers;


/**
 * 用例工厂类
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory {


    @Inject
    HdTaskRepository taskRepository;

    @Inject
    HdTaskLogRepository taskLogRepository;

    @Inject
    OrderRepository orderRepository;
    @Inject
    ProductRepository productRepository;
    @Inject
    XiankangRepository xiankangRepository;
    @Inject
    QuotationRepository quotationRepository;
    @Inject
    StockRepository stockRepository;

    private UseCaseFactory() {


        Guice.createInjector(new HdTaskModule(), new QuotationModule(), new OrderModule(), new ProductModule(), new StockModule()).injectMembers(this);

    }


    public static UseCaseFactory factory = null;


    public synchronized static UseCaseFactory getInstance() {


        if (factory == null) {

            factory = new UseCaseFactory();

        }
        return factory;
    }


    public UseCase createQuotationDetail(long qutationId) {


        return new GetQuotationDetail(Schedulers.newThread(), Schedulers.immediate(), qutationId, quotationRepository);
    }


    public UseCase createProductByNameBetween(String startName, String endName, boolean withCopy) {


        return new ProductUseCase(Schedulers.newThread(), Schedulers.immediate(), startName, endName, withCopy, productRepository);
    }


    public UseCase createUpdateXiankang() {

        return new UpdateXiankangUseCase(Schedulers.newThread(), Schedulers.immediate(), xiankangRepository);
    }


    public UseCase readTaskListUseCase() {

        return new HdTaskListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskRepository);
    }

    public UseCase addHdTaskUseCase(HdTask hdTask) {

        return new HdTaskAddUseCase(Schedulers.newThread(), Schedulers.immediate(), hdTask, taskRepository);
    }


    public UseCase deleteHdTaskUseCase(long taskId) {


        return new HdTaskDeleteUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskRepository);
    }

    public UseCase findTaskLogUseCase(long taskId) {

        return new HdTaskLogListUseCase(Schedulers.newThread(), Schedulers.immediate(), taskId, taskLogRepository);

    }

    public UseCase createProductByNameRandom(String productList, boolean withCopy) {

        return new ProductRandomUseCase(Schedulers.newThread(), Schedulers.immediate(), productList, withCopy, productRepository);
    }


    public UseCase createOrderListUseCase(String key, int pageIndex, int pageSize) {

        return new GetOrderListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, orderRepository);
    }

    public UseCase createOrderItemListUseCase(String os_no) {


        return new GetOrderItemListUseCase(Schedulers.newThread(), Schedulers.immediate(), os_no, orderRepository);
    }

    public UseCase createGetProductByPrdNo(String prdNo) {
        return new GetProductByPrdNoUseCase(Schedulers.newThread(), Schedulers.immediate(), prdNo, productRepository);
    }


    /**
     * 读取出库列表case
     *
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public UseCase createStockOutListUseCase(String key, int pageIndex, int pageSize) {

        return new GetStockOutListUseCase(Schedulers.newThread(), Schedulers.immediate(), key, pageIndex, pageSize, stockRepository);
    }


    /**
     * 读取出库列表case
     *
     * @param ck_no 出库单号

     * @return
     */
    public UseCase createStockOutDetailUseCase(String ck_no ) {

        return new GetStockOutDetailUseCase(Schedulers.newThread(), Schedulers.immediate(), ck_no, stockRepository);
    }
}
