package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.AppVersion;
import com.giants3.hd.utils.entity.ErpOrderItemProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * 订单生产流程状态表
* Created by davidleen29 on 2017/06/17
*/
public interface ErpOrderItemProcessRepository extends JpaRepository<ErpOrderItemProcess,Long> {




    ErpOrderItemProcess findFirstByOsNoEqualsAndItmEqualsAndCurrentWorkFlowStepEquals(String osNo, int itm,int flowStep);
    ErpOrderItemProcess findFirstByMrpNoEquals(String mrpNo );
//    List<ErpOrderItemProcess> findBySentQtyLessThanQty(    );
        @Query(" select p from T_ErpOrderItemProcess  p where  sentQty< qty")
    List<ErpOrderItemProcess> findUnCompleteProcesses(    );


}
