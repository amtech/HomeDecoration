package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 从第三方数据库读取出库单数据相关
 */
public class ErpStockOutRepository {


    public static final String KEY_CK_NO = "ck_no";
    private EntityManager em;

    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */

    public static final String STOCK_OUT_ITEM_LIST = " select B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,B.UP,B.QTY,B.AMT,A.SO_ZXS,A.XS,A.KHXG,A.XGTJ,A.ZXGTJ,A.JZ1,A.MZ from TF_CK_Z A FULL JOIN TF_CK B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM  where A.ck_no=:ck_no  ";


    public static final String STOCK_OUT_LIST="  select A.CK_NO,A.CK_DD,A.CUS_NO,B.MDG,B.TDH,B.GSGX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  ";
    public static final String STOCK_OUT_FIND="  select A.CK_NO,A.CK_DD,A.CUS_NO,B.MDG,B.TDH,B.GSGX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID where A.CK_NO = :ck_no ";


    public ErpStockOutRepository(EntityManager em) {
        this.em = em;
    }
    /*
    *   查询出库单明细列表
     */
    public List<ErpStockOutItem> stockOutItemsList(String ck_no ) {


        Query query = em.createNativeQuery(STOCK_OUT_ITEM_LIST)
               .setParameter(KEY_CK_NO, ck_no) ;
        List<ErpStockOutItem> orders = query.unwrap(SQLQuery.class).addScalar("prd_no", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("qty", FloatType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("so_zxs", StringType.INSTANCE)
                .addScalar("xs", StringType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("xgtj", StringType.INSTANCE)
                .addScalar("jz1", FloatType.INSTANCE)
                .addScalar("mz", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpStockOutItem.class))  .list();
        return orders;

    }

    /*
      *   查询出库单明细列表
       */
    public List<ErpStockOut> stockOutList(String os_no, int pageIndex, int pageSize) {


        Query query = em.createNativeQuery(STOCK_OUT_LIST)
               // .setParameter("OS_ID", KEY_ORDER).setParameter("OS_NO", "%" + os_no + '%')
                ;
        List<ErpStockOut> orders = query.unwrap(SQLQuery.class).addScalar("ck_no", StringType.INSTANCE)
                .addScalar("ck_dd", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("mdg", StringType.INSTANCE)
                .addScalar("tdh", StringType.INSTANCE)
                .addScalar("gsgx", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class)).setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();
        return orders;

    }



    /*
        *   查询出库单明细列表
         */
    public ErpStockOut findStockOut(String ck_no ) {


        Query query = em.createNativeQuery(STOCK_OUT_FIND)
                .setParameter("ck_no", ck_no)
                ;
        List<ErpStockOut> stockOuts = query.unwrap(SQLQuery.class).addScalar("ck_no", StringType.INSTANCE)
                .addScalar("ck_dd", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("mdg", StringType.INSTANCE)
                .addScalar("tdh", StringType.INSTANCE)
                .addScalar("gsgx", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class)).list() ;




        return stockOuts==null||stockOuts.size()==0?null:stockOuts.get(0);

    }



}
