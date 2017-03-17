package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.server.entity_erp.Prdt;
import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Zhilingdan;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 从第三方数据库  数据相关
 * 物料采购相关的
 */


public class ErpWorkRepository {



    public   String SQL_ZHILINGDAN = "";


    private EntityManager em;

    public ErpWorkRepository(EntityManager em) {

        this.em = em;
        if (StringUtils.isEmpty(SQL_ZHILINGDAN)) {




                SQL_ZHILINGDAN = SqlScriptHelper.readScript("zhilingdan");


        }
    }


    /**
     * 查询指令单完成状态表
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        Query query = em.createNativeQuery(SQL_ZHILINGDAN)
                .setParameter("osname", "%"+osName.trim()+"%")
                .setParameter("startdate", startDate)
                .setParameter("enddate", endDate) ;
        List<Zhilingdan> orders = query.unwrap(SQLQuery.class)
                .addScalar("mo_dd", StringType.INSTANCE)
                .addScalar("mo_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("qty_rsv", IntegerType.INSTANCE)
                .addScalar("mrp_no", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)

                .addScalar("caigou_no", StringType.INSTANCE)
                .addScalar("caigouQty", IntegerType.INSTANCE)
                .addScalar("caigou_dd", StringType.INSTANCE)

                .addScalar("jinhuo_no", StringType.INSTANCE)
                .addScalar("jinhuoQty", IntegerType.INSTANCE)
                .addScalar("jinhuo_dd", StringType.INSTANCE)


                .addScalar("jinhuo_dd", StringType.INSTANCE)
                .addScalar("need_days", FloatType.INSTANCE)

                .addScalar("need_dd", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(Zhilingdan.class))  .list();




        return orders;
    }



}
