package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity.ErpWorkFlowReport;
import com.giants3.hd.utils.entity.WorkFlowEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface ErpWorkFlowReportRepository extends JpaRepository<ErpWorkFlowReport, Long> {



    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEquals(String os_no, String prd_no);
}
