package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.app.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 广交会报价单
 * Created by davidleen29 on 2017/9/17.
 */
public interface AppQuotationRepository extends JpaRepository<Quotation, Long> {


    @Query(value = "select p from  T_AppQuotation  p where  (p.customerCode like :key or p.customerName=:key  or p.qNumber like :key  or p.salesman like :key )  order by p.qDate desc "
            , countQuery = "select count(p.id) from  T_AppQuotation  p where  (p.customerCode like :key or p.customerName=:key  or p.qNumber like :key  or p.salesman like :key ) ")
    Page<Quotation> findByKey(@Param("key") String key, Pageable pageable);


    Quotation findFirstByQNumberLikeOrderByQNumberDesc(String key);
}
