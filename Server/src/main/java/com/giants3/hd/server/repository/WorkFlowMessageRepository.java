package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.WorkFlowMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 流程传递处理信息
 */
public interface WorkFlowMessageRepository extends JpaRepository<WorkFlowMessage, Long> {


    List<WorkFlowMessage> findByStateInAndToFlowStepInAndReceiverIdEquals(int[] i, int[] flowSteps, long userId);

    List<WorkFlowMessage> findByFromFlowStepInOrderByCreateTimeDesc(int[] flowSteps);

    List<WorkFlowMessage> findByOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(String osNo, int itm);

    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndProductNameEqualsAndPVersionEqualsOrderByCreateTimeDesc(int flowStep, String os_no, String prd_no, String pVersion);

    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(int flowStep, String os_no, int itm);
    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(int flowStep, String os_no, int itm);
    List<WorkFlowMessage> findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(int flowStep, String os_no, int itm);

//    /**
//     *  删除指定订单的流程消息
//     * @param orderItemId
//     * @return
//     */
//    int deleteByOrderItemIdEquals(long orderItemId  );

//    @Modifying
//    @Query("update T_WorkFlowMessage p set   p.url=:photoUrl , p.thumbnail=:thumbnail  WHERE p.productId =   :productId ")
//    int updateUrlByProductId(@Param("thumbnail") String thumbnail, @Param("photoUrl") String url,@Param("productId")  long productId);


    List<WorkFlowMessage> findByReceiverIdEqualsOrSenderIdEqualsOrderByReceiveTimeDesc(long userId, long userId2);


    @Query(value="select p from  T_WorkFlowMessage  p where  (p.senderId=:userId or receiverId=:userId ) and  (p.orderName like :key or p.productName like :key )  order by p.receiveTime desc "
    ,countQuery = "select count(p.id) from  T_WorkFlowMessage  p where  (p.senderId=:userId or receiverId=:userId ) and  (p.orderName like :key or p.productName like :key ) "
    )
    Page<WorkFlowMessage> findMyWorkFlowMessages(@Param("userId") long userId, @Param("key") String key , Pageable pageable );


    @Modifying
    @Query("delete T_WorkFlowMessage p where   p.orderName=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);


    List<WorkFlowMessage> findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeAsc(int workFlowStep, String osNo, int itm);
}
