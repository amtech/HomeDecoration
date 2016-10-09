package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.WorkFlowOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品生产流程信息
 */
public interface OrderItemWorkFlowRepository extends JpaRepository<WorkFlowOrderItem, Long> {


    public List<WorkFlowOrderItem> findByOrderItemIdEquals(long orderItemId);

    public WorkFlowOrderItem findFirstByOrderItemIdEquals(long orderItemId);

    public List<WorkFlowOrderItem> findByOrderNameEquals(String osNo);

    List<WorkFlowOrderItem> findByWorkFlowStepInAndWorkFlowStateEqualsOrderByOrderNameDesc(int[] steps, int flowState);


    List<WorkFlowOrderItem> findByWorkFlowStepNotOrderByCreateTimeDesc(int finalStep);


    Page<WorkFlowOrderItem> findByOrderNameLikeOrProductNameLikeOrderByOrderNameDesc(String key, String key1, Pageable pageable);

}
