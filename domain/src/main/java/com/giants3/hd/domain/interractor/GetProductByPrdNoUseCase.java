package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;
import rx.Scheduler;

/**根据货号查询 产品详细信息
 * Created by david on 2015/10/7.
 */
public class GetProductByPrdNoUseCase extends UseCase {


    private String prdNo;
    private ProductRepository productRepository;

    public GetProductByPrdNoUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String prdNo, ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);

        this.prdNo = prdNo;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.loadByProductNo(prdNo);
    }
}
