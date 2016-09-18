package com.giants3.hd.server.repository;


import com.giants3.hd.server.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    /**
     * 更新所有产品的生产流程数据， 仅仅在生产流程数据初始化时候 执行。
     * @param workFlowIndexs
     */
    @Modifying
    @Query("update T_Product p set    p.workFlowIndexs=:workFlowIndexs ,p.workFlowNames=:workFlowNames")
    public void setDefaultWorkFlowIds(@Param("workFlowIndexs") String workFlowIndexs ,@Param("workFlowNames") String workFlowNames );
}
