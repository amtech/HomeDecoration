package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;
import rx.Scheduler;

/** 订单报表查询用例
 * Created by david on 2015/10/7.
 */
public class UnCompleteOrderWorkFlowReportUseCase extends UseCase {



    private OrderRepository orderRepository;


    public UnCompleteOrderWorkFlowReportUseCase(Scheduler threadExecutor, Scheduler postExecutionThread,  OrderRepository orderRepository) {
        super(threadExecutor, postExecutionThread);


        this.orderRepository = orderRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.getUnCompleteOrderWorkFlowReport(  );
    }
}
