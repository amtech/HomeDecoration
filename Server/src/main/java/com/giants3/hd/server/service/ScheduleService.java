package com.giants3.hd.server.service;

import com.giants3.hd.server.component.TaskJob;
import com.giants3.hd.server.interf.Job;
import com.giants3.hd.server.repository.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by davidleen29 on 2016/8/15.
 */
@Service
public class ScheduleService extends AbstractService {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    Job taskJob;

    /**
     * 更新附件路径  定时将附件
     */
    //@Scheduled(fixedDelay = 5000)
    public void updateAttaches() {
        System.out.println("processing next 10 at " + new Date());
        for (int i = 0; i < 10; i++) {
            taskJob.doTask(counter.incrementAndGet());
        }

    }
}
