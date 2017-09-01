package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItemWorkState;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单的生产状态
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemWorkStateRepository extends JpaRepository<OrderItemWorkState,Long> {


    OrderItemWorkState findFirstByOsNoEqualsAndItmEquals(String osNo, int itm);
}
