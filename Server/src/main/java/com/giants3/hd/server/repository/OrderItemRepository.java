package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.OrderItem;
import com.giants3.hd.server.entity.StockOutItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 出库附加数据细项
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


   OrderItem findFirstByOsNoEqualsAndItmEquals(String os_no, int item);

}
