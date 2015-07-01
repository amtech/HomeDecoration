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




    Page<Product> findByNameLikeOrderByNameAsc(String proName, Pageable pageable);


    /**
     * 产品由产品名称与 产品版本号  共同作为识别码
     * @param proName
     * @param version
     * @return
     */
    Product findFirstByNameEqualsAndPVersionEquals(String proName, String version);


}
