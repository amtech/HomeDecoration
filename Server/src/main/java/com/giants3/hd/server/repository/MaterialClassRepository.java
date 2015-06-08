package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.PackMaterialClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  材料类型 根据编码头四位进行区分
 *
*/
public interface MaterialClassRepository extends JpaRepository<MaterialClass,Long> {

}
