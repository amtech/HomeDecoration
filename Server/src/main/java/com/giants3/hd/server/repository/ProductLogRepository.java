package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.ProductLog;
import com.giants3.hd.utils.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface ProductLogRepository extends JpaRepository<ProductLog,Long> {




    public ProductLog findFirstByProductIdEquals(long productId);
}
