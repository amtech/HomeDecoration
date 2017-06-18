package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.WorkFlowArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  流程交接区域
 *
*/
public interface WorkFlowAreaRepository extends JpaRepository<WorkFlowArea,Long> {





}
