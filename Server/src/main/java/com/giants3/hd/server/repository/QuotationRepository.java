package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.PClass;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 报价
 *
*/
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

    Page<Quotation> findByCustomerNameLike(String name, Pageable pageable);
}
