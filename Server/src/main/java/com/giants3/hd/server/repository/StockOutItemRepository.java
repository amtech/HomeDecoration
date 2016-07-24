package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.StockOut;
import com.giants3.hd.server.entity.StockOutItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 出库附加数据细项
* Created by davidleen29 on 2014/9/17.
*/
public interface StockOutItemRepository extends JpaRepository<StockOutItem,Long> {


   StockOutItem findFirstByCkNoEqualsAndItmEquals(String ck_no,int item);

}
