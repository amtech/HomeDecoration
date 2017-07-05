package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * 订单生产流程状态表
* Created by davidleen29 on 2017/06/17
*/
public interface ErpOrderItemProcessRepository extends JpaRepository<ErpOrderItemProcess,Long> {




    ErpOrderItemProcess findFirstByOsNoEqualsAndItmEqualsAndCurrentWorkFlowStepEquals(String osNo, int itm,int flowStep);
    ErpOrderItemProcess findFirstByMrpNoEquals(String mrpNo );
    ErpOrderItemProcess findFirstByMoNoEqualsAndMrpNoEquals(String mono ,String mrpNo);
//    List<ErpOrderItemProcess> findBySentQtyLessThanQty(    );
        @Query(" select p from T_ErpOrderItemProcess  p where  sentQty< qty")
    List<ErpOrderItemProcess> findUnCompleteProcesses(    );



//
//   // @Query(" select distinct o from T_ErpOrderItemProcess  p , T_OrderItem o where   o.osNo=p.osNo and o.itm=p.itm  and o.workFlowState!="+ ErpWorkFlow.STATE_COMPLETE +" ")
//    @Query(" select   o from   T_OrderItem o where     o.workFlowState="+ ErpWorkFlow.STATE_WORKING +" and (o.osNo like  :key  or  o.prdNo like  :key  )")
//    List<OrderItem> findAllProcessUnComplete(@Param("key")     String key   );
//

}
