package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.app.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 广交会报价单
 * Created by davidleen29 on 2017/9/17.
 */
public interface AppQuotationRepository extends JpaRepository<Quotation, Long> {


    Page<Quotation> findByCustomerLikeOrQNumberLikeOrderByQDateDesc(String key, String key1, Pageable pageable);
}
