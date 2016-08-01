package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
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

    public static final String STOCK_OUT_ITEM_LIST = " select B.ck_no,B.itm, B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,isnull(B.UP,0) as UP, isnull(B.QTY,0) as  QTY, isnull(B.AMT,0) as   AMT ,A.SO_ZXS,A.XS,A.KHXG,A.XGTJ,A.ZXGTJ,isnull(A.JZ1,0)as JZ1,isnull(A.MZ,0) as MZ  from TF_CK_Z A FULL JOIN TF_CK B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM  where A.ck_no=:ck_no  order by B.itm ASC  ";



    //搜索基句
    public static final String  STOCK_OUT_LIST=" select A.CK_NO,A.CK_DD,A.CUS_NO,B.MDG,B.TDH,B.GSGX ,C.ADR2,C.TEL1,C.FAX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO  ";

    //按日期排序
    public static final String STOCK_OUT_ORDER=" order by  A.ck_dd desc ";
    public static final String STOCK_OUT_SEARCH=STOCK_OUT_LIST +" where A.CUS_NO like :cus_no or A.CK_NO like :ck_no " +STOCK_OUT_ORDER;
    public static final String STOCK_OUT_FIND=STOCK_OUT_LIST+" where A.CK_NO = :ck_no " +STOCK_OUT_ORDER;


    public ErpStockOutRepository(EntityManager em) {
        this.em = em;
    }
    /*
    *   查询出库单明细列表
     */
    public List<ErpStockOutItem> stockOutItemsList(String ck_no ) {


        Query query = em.createNativeQuery(STOCK_OUT_ITEM_LIST)
               .setParameter(KEY_CK_NO, ck_no) ;
        List<ErpStockOutItem> orders = query.unwrap(SQLQuery.class)
                .addScalar("ck_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("qty", FloatType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("so_zxs", StringType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
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
    public List<ErpStockOut> stockOutList(String key, int pageIndex, int pageSize) {



        org.hibernate.Query query= getStockOutListQuery(STOCK_OUT_SEARCH);

        query.setParameter("ck_no", "%" + key + '%').setParameter("cus_no", "%" + key + '%')
                ;
        List<ErpStockOut> orders = query
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();

        modifyDateString(orders);

        return orders;

    }


    /**
     * 调整日期数据
     * @param data
     */
    protected void modifyDateString(List<ErpStockOut> data)
    {
        if(data==null) return ;
        for(ErpStockOut item:data)
        {

            if(!StringUtils.isEmpty(item.ck_dd))
            {
                if(item.ck_dd.length()>10)
                {
                    item.ck_dd=item.ck_dd.substring(0,10);
                }
            }
        }
    }


    private org.hibernate.Query getStockOutListQuery(String sql)
    {
        Query query = em.createNativeQuery(sql);
     return    query.unwrap(SQLQuery.class).addScalar("ck_no", StringType.INSTANCE)
                .addScalar("ck_dd", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("mdg", StringType.INSTANCE)
                .addScalar("tdh", StringType.INSTANCE)
                .addScalar("gsgx", StringType.INSTANCE)
                .addScalar("adr2", StringType.INSTANCE)
                .addScalar("tel1", StringType.INSTANCE)
                .addScalar("fax", StringType.INSTANCE)
             .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class));

    }

    /*
        *   查询出库单明细列表
         */
    public ErpStockOut findStockOut(String ck_no ) {


        org.hibernate.Query query = getStockOutListQuery(STOCK_OUT_FIND)
                .setParameter("ck_no", ck_no);
        List<ErpStockOut> stockOuts = query.list() ;
        modifyDateString(stockOuts);

        return stockOuts==null||stockOuts.size()==0?null:stockOuts.get(0);

    }



}
