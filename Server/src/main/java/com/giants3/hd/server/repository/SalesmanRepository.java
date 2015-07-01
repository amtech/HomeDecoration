package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  业务员信息
 *
*/
public interface SalesmanRepository extends JpaRepository<Salesman,Long> {

}
