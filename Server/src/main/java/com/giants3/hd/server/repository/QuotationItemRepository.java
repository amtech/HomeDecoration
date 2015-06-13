package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 报价
 *
*/
public interface QuotationItemRepository extends JpaRepository<QuotationItem,Long> {

   QuotationItem findFirstByProductIdEquals(long productId);
}
