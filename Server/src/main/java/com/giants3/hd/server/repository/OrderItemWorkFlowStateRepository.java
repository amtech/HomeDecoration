package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by david on 2015/11/5.
 */
public interface OrderItemWorkFlowStateRepository extends JpaRepository<OrderItemWorkFlowState, Long> {


    List<OrderItemWorkFlowState> findByQtyIsGreaterThanOrderByOrderNameDescCreateTimeDesc(int qty);

    List<OrderItemWorkFlowState> findByQtyIsGreaterThanAndOrderItemIdEqualsOrderByOrderNameDescCreateTimeDesc(int qty, long orderItemId);

    List<OrderItemWorkFlowState> findByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsOrderByOrderNameDescCreateTimeDesc(int qty, long orderItemId, int workFlowStep);

    Page<OrderItemWorkFlowState> findByOrderNameLikeOrProductFullNameLikeOrderByOrderNameDescCreateTimeDesc(String orderLike, String prdNameLike, Pageable pageable);

    Page<OrderItemWorkFlowState> findByOrderNameLikeAndQtyIsGreaterThanOrProductFullNameLikeAndQtyIsGreaterThanOrderByOrderNameDescCreateTimeDesc(String orderLike, int qty, String prdNameLike, int qty2, Pageable pageable);

    List<OrderItemWorkFlowState> findByWorkFlowStepInOrderByOrderNameDesc(int[] steps);

    List<OrderItemWorkFlowState> findByWorkFlowStepInAndQtyIsGreaterThanOrderByOrderNameDesc(int[] steps, int qty);

    OrderItemWorkFlowState findFirstByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsAndFactoryIdEqualsOrderByOrderNameDescCreateTimeDesc(int qty, long orderItemId, int workFlowStep, long factoryId);

    OrderItemWorkFlowState findFirstByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsAndFactoryIdEqualsAndProductTypeEqualsOrderByOrderNameDescCreateTimeDesc(int qty, long orderItemId, int nextWorkFlowStep, String factoryId, String productType);


    /**
     * 查找指定订单 制定流程的 流程状态数据
     *
     * @return
     */
    List<OrderItemWorkFlowState> findByOrderItemIdEqualsAndWorkFlowStepEquals(long orderItemId, int workFlowStep);

    /**
     * 查找指定订单 制定流程的 流程状态数据 并且未发送数量大于0
     *
     * @return
     */
    List<OrderItemWorkFlowState> findByOrderItemIdEqualsAndWorkFlowStepEqualsAndUnSendQtyGreaterThan(long orderItemId, int workFlowStep, int qty);
//    List<OrderItemWorkFlowState> findByOrderItemIdEqualsAndWorkFlowStepEqualsAndUnSendQtyGreaterThanAndNextWorkFlowStepGreaterThan(long orderItemId, int workFlowStep,int qty);


    /**
     * s删除所有订单相关的流程记录数据
     */
      int deleteByOrderItemIdEquals(long orderItemId);
}
