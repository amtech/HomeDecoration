package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.OrderItem;
import com.giants3.hd.utils.entity.OrderItemWorkMemo;
import com.giants3.hd.utils.entity.OrderItemWorkState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单的生产状态
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemWorkStateRepository extends JpaRepository<OrderItemWorkState,Long> {


    OrderItemWorkState findFirstByOsNoEqualsAndItmEquals(String osNo, int itm);
}
