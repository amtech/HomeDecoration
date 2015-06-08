package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.PackMaterialPosition;
import com.giants3.hd.utils.entity.PackMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 包装材料使用位置
 *
*/
public interface PackMaterialPositionRepository extends JpaRepository<PackMaterialPosition,Long> {

}