package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.ProductWage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 产品工资资源类资源 。
 *
 * 提供根据产品id查询相关信息
 *
*/
public interface ProductWageRepository extends JpaRepository<ProductWage,Long> {


    public List<ProductWage> findByProductIdEquals(long productId);
    public List<ProductWage> findByProductIdEqualsAndFlowIdEquals(long productId, long flowId);
}
