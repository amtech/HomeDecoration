package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.entity.QuotationXKItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  咸康报价
 *
*/
public interface QuotationXKItemRepository extends JpaRepository<QuotationXKItem,Long> {

   public QuotationXKItem findFirstByProductIdEquals(long productId);

   public List<QuotationXKItem>  findByQuotationIdEquals(long quotationId);
   int  deleteByQuotationIdEquals(long quotationId);
}
