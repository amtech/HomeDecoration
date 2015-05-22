package com.giants3.hd.server.repository;


import com.giants3.hd.utils.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
*  产品资源库
*/
public interface ProductRepository extends JpaRepository<Product,Long> {




    Page<Product> findByNameLike(  String proName,Pageable pageable);


}
