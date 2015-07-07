package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  客户信息
 *
*/
public interface CustomerRepository extends JpaRepository<Customer,Long> {


    public Customer findFirstByCodeEquals(String code);

}
