package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.WorkFlowOrderItem;
import com.giants3.hd.server.entity.OrderItemWorkFlow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 产品生产流程信息
 */
public interface OrderItemWorkFlowRepository2 extends JpaRepository<OrderItemWorkFlow, Long> {


    OrderItemWorkFlow findFirstByOrderItemIdEquals(long orderItemId);
}
