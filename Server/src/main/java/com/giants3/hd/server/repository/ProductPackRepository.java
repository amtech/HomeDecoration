package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductPack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  包装信息  一个产品 有两个包装数据
 *
*/
public interface ProductPackRepository extends JpaRepository<ProductPack,Long> {


    public List<ProductPack> findByProductIdEquals(long productId);

}
