package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;
import rx.Scheduler;

/** 订单报表查询用例
 * Created by david on 2015/10/7.
 */
public class GetOrderReportUseCase extends UseCase {



    private OrderRepository orderRepository;

    private String salCode;
    private final String dateStart;
    private final String dateEnd;
    private int pageIndex;

    private int pageSize;
    public GetOrderReportUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String salCode,   String dateStart,String dateEnd, int pageIndex, int pageSize, OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);
        this.salCode = salCode;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.pageIndex = pageIndex;

        this.pageSize = pageSize;

        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getOrderReport(salCode,dateStart,dateEnd,pageIndex,pageSize);
    }
}
