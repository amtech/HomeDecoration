package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.app.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 广交会报价单
 * Created by davidleen29 on 2017/9/17.
 */
public interface AppQuotationItemRepository extends JpaRepository<QuotationItem, Long> {

    List<QuotationItem> findByQuotationIdEqualsOrderByIndexAsc(long quotationId);
    QuotationItem findFirstByQuotationIdEqualsAndIndexEquals (long quotationId,int index);


}
