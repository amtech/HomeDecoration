package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.entity.QuotationXKItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
*  咸康报价
 *
*/
public interface QuotationXKItemRepository extends JpaRepository<QuotationXKItem,Long> {

   public QuotationXKItem findFirstByProductIdEquals(long productId);

   public List<QuotationXKItem> findByQuotationIdEqualsOrderByIIndex(long quotationId);
   int  deleteByQuotationIdEquals(long quotationId);

   @Modifying
   @Query("update T_QuotationXKItem p set    p.productPhoto=:productPhoto ,p.photoUrl=:photoUrl    WHERE p.productId =   :productId ")
   public int updatePhotoByProductId(@Param("productPhoto") byte[] productPhoto,@Param("photoUrl") String photoUrl,@Param("productId") long productId);

   @Modifying
   @Query("update T_QuotationXKItem p set    p.productPhoto2=:productPhoto ,p.photo2Url=:photo2Url    WHERE p.productId2 =   :productId ")
   public int updatePhoto2ByProductId(@Param("productPhoto") byte[] productPhoto,@Param("photo2Url") String photo2Url,@Param("productId") long productId);

}
