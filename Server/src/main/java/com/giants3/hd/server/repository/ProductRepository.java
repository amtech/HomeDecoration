package com.giants3.hd.server.repository;


import com.giants3.hd.server.entity.Prdt1;
import com.giants3.hd.server.entity.Product;
import com.giants3.hd.server.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
*  产品资源库
*/
public interface ProductRepository extends JpaRepository<Product,String> {

    @Query(value = " SELECT a FROM Product a WHERE  a.id=:prd_no     ")
     Product  findByPrdId(@Param("prd_no") long prdt_no);

    @Query(value = " SELECT  a    FROM Product a WHERE  a.name like  :prd_no    " )
    Page<Product> findByPrd_noLike(@Param("prd_no") String prdt_no,Pageable pageable);


}
