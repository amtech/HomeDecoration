package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.WorkFlowMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 流程传递处理信息
 */
public interface WorkFlowMessageRepository extends JpaRepository<WorkFlowMessage, Long> {


    List<WorkFlowMessage> findByStateInAndToFlowStepIn(int[] i, int[] flowSteps);

    List<WorkFlowMessage> findByFromFlowStepIn(int[] flowSteps);
}
