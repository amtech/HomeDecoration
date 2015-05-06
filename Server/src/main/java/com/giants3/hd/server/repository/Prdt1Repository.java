package com.giants3.hd.server.repository;


import com.giants3.hd.server.entity.Prdt1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
*  产品资源库
*/
public interface Prdt1Repository extends JpaRepository<Prdt1,String> {

    @Query(value = " SELECT a FROM PRDT1 a WHERE  a.prd_no=:prd_no  and a.qty>0  ")
    List<Prdt1> findByPrd_no(@Param("prd_no") String prdt_no);

    @Query(value = " SELECT  a    FROM PRDT1 a WHERE  a.prd_no like  :prd_no   and a.qty>0   " )
    List<Prdt1> findByPrd_noLike(@Param("prd_no") String prdt_no);

}
