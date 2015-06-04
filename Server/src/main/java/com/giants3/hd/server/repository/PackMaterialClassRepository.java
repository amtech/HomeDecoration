package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.PackMaterialClass;
import com.giants3.hd.utils.entity.PackMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 包装材料类型
 *
*/
public interface PackMaterialClassRepository extends JpaRepository<PackMaterialClass,Long> {

}
