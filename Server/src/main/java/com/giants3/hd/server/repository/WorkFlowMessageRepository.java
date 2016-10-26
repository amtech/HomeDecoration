package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.WorkFlowMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 流程传递处理信息
 */
public interface WorkFlowMessageRepository extends JpaRepository<WorkFlowMessage, Long> {


    List<WorkFlowMessage> findByStateInAndToFlowStepIn(int[] i, int[] flowSteps);

    List<WorkFlowMessage> findByFromFlowStepInOrderByCreateTimeDesc(int[] flowSteps);

    @Modifying
    @Query("update T_WorkFlowMessage p set   p.url=:photoUrl , p.thumbnail=:thumbnail  WHERE p.productId =   :productId ")
    int updateUrlByProductId(@Param("thumbnail") String thumbnail, @Param("photoUrl") String url,@Param("productId")  long productId);
}