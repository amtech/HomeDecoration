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

   // Page<Quotation> findByCustomerNameLikeOrQDateLikeOrSalesmanLikeOrderByQNumberDesc(String name, String qNumber,String salesman,Pageable pageable);

    Page<Quotation> findByCustomerNameLikeAndSalesmanIdEqualsOrQNumberLikeAndSalesmanIdEqualsOrderByQNumberDesc(String customerName,long salesManId, String qNumber,long salesManId2,Pageable pageable);
    Page<Quotation> findByCustomerNameLikeOrQNumberLikeOrderByQNumberDesc(String customerName, String qNumber,Pageable pageable);

    Quotation findFirstByqNumberEquals(String qNumber);


}
