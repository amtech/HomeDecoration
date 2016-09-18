package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.Factory;
import com.giants3.hd.server.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  生产流程信息
 *
*/
public interface WorkFlowRepository extends JpaRepository<WorkFlow,Long> {


    List<WorkFlow> findByCheckerIdEqualsOrUserIdEquals(long id, long id1);
}
