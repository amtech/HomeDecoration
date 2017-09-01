package com.giants3.hd.server.repository;

import com.giants3.hd.entity.ErpWorkFlowReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface ErpWorkFlowReportRepository extends JpaRepository<ErpWorkFlowReport, Long> {



    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEquals(String os_no, String prd_no);
    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEqualsAndPVersionEquals(String os_no, String prd_no,String pVersion);
    List<ErpWorkFlowReport> findByOsNoEqualsAndItmEquals(String os_no,int itm);

     ErpWorkFlowReport  findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(String osNo,int itm,int fromFlowStep);
}
