package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 产品材料资源 。
 *
 * 提供根据产品id查询相关信息
 *
*/
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial,Long> {


    public List<ProductMaterial> findByProductIdEquals(long productId);
    public List<ProductMaterial> findByProductIdEqualsAndFlowIdEquals(long productId,long flowId);
}
