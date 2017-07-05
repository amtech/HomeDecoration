package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity.ErpOrderItem;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.Query;

/**
 * Created by davidleen29 on 2017/6/10.
 */
public class ErpRepository {
    protected org.hibernate.Query getOrderItemListQuery(Query query)
    {


        org.hibernate.Query hQuery=   query .unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("htxs", IntegerType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("xgtj", FloatType.INSTANCE)
                .addScalar("zxgtj", FloatType.INSTANCE)
                .addScalar("hpgg", StringType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)
                .addScalar("os_dd", StringType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItem.class));
        return hQuery;

    }
}
