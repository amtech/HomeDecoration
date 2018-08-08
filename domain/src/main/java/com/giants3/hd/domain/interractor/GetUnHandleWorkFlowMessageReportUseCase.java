package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.WorkFlowRepository;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/7/7.
 */
public class GetUnHandleWorkFlowMessageReportUseCase extends DefaultUseCase {
    private final int hourLimit;
    private final WorkFlowRepository workFlowRepository;

    public GetUnHandleWorkFlowMessageReportUseCase(int hourLimit, WorkFlowRepository workFlowRepository) {
        super();
        this.hourLimit = hourLimit;
        this.workFlowRepository = workFlowRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return workFlowRepository.getUnHandleWorkFlowMessageReport(hourLimit);


    }
}
