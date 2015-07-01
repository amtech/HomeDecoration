package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 报价
 *
*/
public interface QuotationItemRepository extends JpaRepository<QuotationItem,Long> {

   public QuotationItem findFirstByProductIdEquals(long productId);

   public List<QuotationItem>  findByQuotationIdEquals(long quotationId);
   int  deleteByQuotationIdEquals(long quotationId);
}
