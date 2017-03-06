package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.OutFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 加工户信息
 */

public class OutFactoryRepository {


    //查找所有的加工户信息
    private static final java.lang.String FIND_ALL = "select DEP,NAME from DEPT where MAKE_ID=3  order by dep asc";

    private EntityManager manager;

    public OutFactoryRepository(EntityManager manager) {


        this.manager = manager;
    }


    public List<OutFactory> findAll() {


        Query query = manager.createNativeQuery(FIND_ALL);
        List<OutFactory> orders = query.unwrap(SQLQuery.class)
                .addScalar("dep", StringType.INSTANCE)
                .addScalar("name", StringType.INSTANCE)

                .setResultTransformer(Transformers.aliasToBean(OutFactory.class)).list();


        return orders;


    }
}
