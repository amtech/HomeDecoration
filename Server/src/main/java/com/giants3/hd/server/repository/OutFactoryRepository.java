package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.Factory;
import com.giants3.hd.server.entity.OutFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
*  客户信息
 *
*/

public interface OutFactoryRepository extends JpaRepository<OutFactory,Long> {


    OutFactory findFirstByNameEquals(String name);
}
