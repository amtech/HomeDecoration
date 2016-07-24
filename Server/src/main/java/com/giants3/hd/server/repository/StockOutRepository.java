package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.HdTask;
import com.giants3.hd.server.entity.HdTaskLog;
import com.giants3.hd.server.entity.StockOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 出库附加数据
* Created by davidleen29 on 2014/9/17.
*/
public interface StockOutRepository extends JpaRepository<StockOut,Long> {


   StockOut findFirstByCkNoEquals(String ck_no);

}