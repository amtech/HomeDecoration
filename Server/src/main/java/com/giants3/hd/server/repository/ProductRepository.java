package com.giants3.hd.server.repository;


import com.giants3.hd.server.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
*  产品资源库
*/
public interface ProductRepository extends JpaRepository<Product,Long> {




    Page<Product> findByNameLikeOrPVersionLikeOrderByNameAsc(String proName,String pVersion, Pageable pageable);


    /**
     *
     * @param startName
     * @param endName
     * @return
     */
    List<Product> findByNameBetweenOrderByName(String startName, String endName);

    /**
     * 产品由产品名称与 产品版本号  共同作为识别码
     * @param proName
     * @param version
     * @return
     */
    Product findFirstByNameEqualsAndPVersionEquals(String proName, String version);

    /**
     * 查找相同产品品名的货物
     * @param proName
     * @return
     */
    List<Product> findByNameEquals(String proName);
}
