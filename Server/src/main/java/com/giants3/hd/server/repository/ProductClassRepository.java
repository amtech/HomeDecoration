package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.PClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 材料
 *
*/
public interface ProductClassRepository extends JpaRepository<PClass,Long> {

}
