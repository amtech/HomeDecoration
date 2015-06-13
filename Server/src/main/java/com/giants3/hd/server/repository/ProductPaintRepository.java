package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.ProductPaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 产品油漆信息资源 。
 *
 * 提供根据产品id查询相关信息
 *
*/
public interface ProductPaintRepository extends JpaRepository<ProductPaint,Long> {


    public List<ProductPaint> findByProductIdEquals(long productId);

    public List<ProductPaint>   findByMaterialIdEquals(long materialId);

    public ProductPaint   findFirstByMaterialIdEquals(long materialId);

}
