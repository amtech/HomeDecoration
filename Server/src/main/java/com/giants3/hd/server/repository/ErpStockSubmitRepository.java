package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity.GlobalData;
import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.StockSubmit;
import com.giants3.hd.utils.entity.StockXiaoku;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进货单里的数据是外厂到仓库的信息，缴库单的数据是车间到仓库的信息，销货单的数据是仓库到装柜的信息，
 */
public class ErpStockSubmitRepository {

//进货单表身
//    select * FROM  TF_PSS WHERE PS_ID='PC' AND WH='CP'





    private static final String ID_NO = "ID_NO";
    public static final  String stockInSql= "select prd_mark as so_no ,PRD_NO,PRD_NAME, PRD_MARK, ID_NO  ,BAT_NO,CUS_OS_NO,QTY  from TF_PSS WHERE  PS_ID='PC' AND WH='CP'  and  PRD_MARK like '%YF%' and  ID_NO  is not null ";

    //缴库单
 //   select * FROM  TF_MM0 WHERE WH='CP'

    public static final  String submitSql= "select SO_NO ,PRD_NO, PRD_NAME  ,ID_NO  ,BAT_NO,CUS_OS_NO,QTY from TF_MM0 where wh='CP'";

   //材料信息
    public static final  String bomSql= "select BOM_NO ,KHXG,SO_ZXS,XGTJ from TF_MF_BOM_Z  where KHXG IS NOT NULL   and bom_no =:" + ID_NO + " ";





    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";


    /**
     * 查询厂家入库记录=
     */
    public static final String  stockInFromFactory="   select ps.ps_no as no, ps.so_no,ps.PRD_NO,ps.PRD_NAME,ps.PRD_MARK,ps.BAT_NO,ps.CUS_OS_NO,ps.qty,ps.dd,ps.type,pos.id_no ,mps.dept from (select prd_mark as so_no ,PRD_NO,PRD_NAME, PRD_MARK, BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,2 as type ,ps_no from TF_PSS   WHERE  PS_ID='PC' AND WH='CP'  and  PRD_MARK like '%YF%'  and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE +" ) as ps inner join ( select os_no ,prd_no,id_no FROM  TF_POS where OS_ID='SO' ) as pos  on ps.PRD_MARK=pos.os_no   and  ps.PRD_NO=pos.PRD_NO    inner join (select  cus_no  as dept ,ps_no from mf_pss  WHERE  PS_ID='PC'      and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE +")    as  mps  on mps.ps_no= ps.ps_no"   ;

    //车间入库记录   缴库
    public  static final String  StockSubmitSql=" select  mm_no as no,SO_NO ,PRD_NO,PRD_NAME,PRD_MARK  ,BAT_NO,CUS_OS_NO,QTY ,mm_dd as dd   ,1 as type , ID_NO ,'' as dept from TF_MM0 where wh='CP'   and mm_dd  >= :" + START_DATE + "  and mm_dd  <= :" + END_DATE  ;

   // +  StockSubmitSql+ "  union     "
    //查询入库与缴库的产品数据
     public  static final String stockInAndSubmitSql= "  select * from (" +  StockSubmitSql+ "  union     "+  stockInFromFactory+"   ) as a  inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO  inner join (select os_no, cus_no from mf_pos ) as c on a.so_no = c.os_no  order by a.dd desc   ";
//出销库单
  //  select * FROM  TF_PSS WHERE PS_ID='SA' AND WH='CP'
public  static final String stockOutTransportSql=  " select * from (select prd_mark as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept  from TF_PSS WHERE  PS_ID='SA' AND WH='CP'  and  PRD_MARK like '%YF%' and  ID_NO  is not null and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + " ) as a   inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO order by a.dd desc ";
public  static final String stockXiaoKuSql=  " select PS_ID   ,PS_NO    ,TCGS  ,XHDG   ,XHFQ  ,XHGH     ,XHGX   ,XHPH from   MF_PSS_Z  WHERE PS_ID='SA' and ps_no like :ps_no  order by ps_no desc ";
public  static final String stockXiaoKuRecordCountSql=  " select  count(*) from   MF_PSS_Z  WHERE PS_ID='SA'    and ps_no like :ps_no   ";

    public  static final String stockXiaokuItemSql=  " select * from (select ps_no as no, prd_mark as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept,ITM  from TF_PSS WHERE  PS_ID='SA' AND WH='CP'   and ps_no  = :ps_no ) as a    inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO  inner join (select os_no, cus_no from mf_pos ) as c on a.so_no = c.os_no order by a.ITM asc ";


    //销库单明细日期区间查询
    public  static final String searchstockXiaokuItemSql=  " select * from (select ps_no as no, prd_mark as so_no ,PRD_NO,PRD_NAME, PRD_MARK ,BAT_NO,CUS_OS_NO,QTY ,ps_dd as dd ,  3 as type, ID_NO ,'' as dept,ITM  from TF_PSS WHERE  PS_ID='SA' AND WH='CP'   and ps_no  like :ps_no   and ps_dd  >= :" + START_DATE + "  and ps_dd  <= :" + END_DATE + " ) as a    inner join  (select BOM_NO ,KHXG,SO_ZXS,XGTJ from MF_BOM_Z  where KHXG IS NOT NULL  )  as b on a.ID_NO=b.BOM_NO  inner join (select os_no, cus_no from mf_pos ) as c on a.so_no = c.os_no order by a.dd desc ";







    public static final String KEY_CK_NO = "ck_no";
    public static final String KEY_SAL_NO = "SAL_NO";
    public static final String CUS_NO = "cus_no";
    public static final String CK_NO = "ck_no";



    private EntityManager em;

    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */

    public static final String STOCK_OUT_ITEM_LIST = " select B.ck_no,B.itm, B.PRD_NO ,b.id_no,b.os_no,b.bat_no,B.CUS_OS_NO,isnull(B.UP,0) as UP, isnull(B.QTY,0) as  QTY, isnull(B.AMT,0) as   AMT ,A.SO_ZXS,A.IDX_NAME,A.XS,A.KHXG,A.XGTJ,A.ZXGTJ,isnull(A.JZ1,0)as JZ1,isnull(A.MZ,0) as MZ  from TF_CK_Z A FULL JOIN TF_CK B ON A.CK_NO=B.CK_NO AND A.ITM=B.ITM  where A.ck_no=:ck_no  order by B.itm ASC  ";



    //搜索基句
    public static final String  STOCK_OUT_LIST=" select A.CK_NO,A.CK_DD,A.CUS_NO,A.sal_no,B.MDG,B.TDH,B.GSGX ,C.ADR2,C.TEL1,C.FAX from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO  ";

    //按日期排序
    public static final String STOCK_OUT_ORDER=" order by  A.ck_dd desc ";
    public static final String STOCK_OUT_SEARCH=STOCK_OUT_LIST +" where A.CUS_NO like :cus_no or A.CK_NO like :ck_no " +STOCK_OUT_ORDER;


    public static final String  SQL_WHERE_SALE_CAUSE=" and a.sal_no in ( :SAL_NO ) ";

    public static final String STOCK_OUT_SEARCH_WITH_SALE=STOCK_OUT_LIST +" where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  "+ SQL_WHERE_SALE_CAUSE +STOCK_OUT_ORDER;

    public static final String STOCK_OUT_COUNT_WITH_SALE="select  count(*) as count from   MF_CK  A FULL  JOIN  MF_CK_Z B  on A.CK_ID=b.CK_ID  and a.ck_no=b.ck_no  LEFT JOIN CUST C on A.CUS_NO=c.CUS_NO    where ( A.CUS_NO like :cus_no or A.CK_NO like :ck_no )  "+ SQL_WHERE_SALE_CAUSE  ;

    public static final String STOCK_OUT_FIND=STOCK_OUT_LIST+" where A.CK_NO = :ck_no " +STOCK_OUT_ORDER;


    public ErpStockSubmitRepository(EntityManager em) {
        this.em = em;
    }
    /*
    *    查询出 进货与缴库 列表  日期参数格式 "2016-07-11"
     */
    public List<StockSubmit> getStockSubmitList(String startDate,String endData ) {


        Query query = em.createNativeQuery(stockInAndSubmitSql)
              .setParameter(START_DATE, startDate)
                .setParameter(END_DATE, endData);
        List<StockSubmit> orders = getStockSubmits(query);


        return orders;

    }/*
    *    查询出库到柜记录  日期参数格式 "2016-07-11"
     */

    private List<StockSubmit> getStockSubmits(Query query) {
        final List<StockSubmit> list = query.unwrap(SQLQuery.class)

                .addScalar("no", StringType.INSTANCE)
                .addScalar("so_no", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("dd", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("type", IntegerType.INSTANCE)
                .addScalar("xgtj", FloatType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("dept", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(StockSubmit.class)).list();




        return list;
    }

    public List<StockSubmit> getStockXiaokuItemList(String key,String startDate,String endData ) {


        Query query = em.createNativeQuery(searchstockXiaokuItemSql)
                .setParameter("ps_no","%"+key.trim()+"%")
              .setParameter(START_DATE, startDate)
                .setParameter(END_DATE, endData);
        List<StockSubmit> orders = getStockSubmits(query);


        return orders;

    }

    public List<StockSubmit> getStockXiaokuItemList( String ps_no ) {


        Query query = em.createNativeQuery(stockXiaokuItemSql)
                .setParameter("ps_no", ps_no);

        List<StockSubmit> orders = getStockSubmits(query);


        return orders;

    }


        /**
         * 获取销库单列表
         * @param pageIndex
         * @param pageSize
         * @return
         */
    public List<StockXiaoku> getStockXiaokuList( String  key,int pageIndex, int pageSize)
    {


        org.hibernate.Query query= getStockXiaokuListQuery(stockXiaoKuSql);

        List<StockXiaoku> orders = query.setParameter("ps_no","%"+key.trim()+"%")
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();

        return orders;
    }

    /*
      *   查询出库单明细列表
       */
    public List<ErpStockOut> stockOutList(String key,List<String> salNos ,int pageIndex, int pageSize) {



        org.hibernate.Query query= getStockOutListQuery(STOCK_OUT_SEARCH_WITH_SALE);

        query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO,salNos);
                ;
        List<ErpStockOut> orders = query
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();

        modifyDateString(orders);

        return orders;

    }


    /**
     * 获取关键字查询记录总数
     *
     * @param key
     * @return
     */
    public int getRecordCountByKey(String key,List<String> saleNos) {


        org.hibernate.Query      query =   em.createNativeQuery(STOCK_OUT_COUNT_WITH_SALE).unwrap(SQLQuery.class) ;

        return (Integer) ( query.setParameter(CK_NO, "%" + key + '%').setParameter(CUS_NO, "%" + key + '%').setParameterList(KEY_SAL_NO,saleNos).list().get(0));
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
                .addScalar("sal_no", StringType.INSTANCE)
                .addScalar("fax", StringType.INSTANCE)
             .setResultTransformer(Transformers.aliasToBean(ErpStockOut.class));

    }
private org.hibernate.Query getStockXiaokuListQuery(String sql)
    {
        Query query = em.createNativeQuery(sql);
     return    query.unwrap(SQLQuery.class).addScalar("ps_id", StringType.INSTANCE)
                .addScalar("ps_no", StringType.INSTANCE)
                .addScalar("tcgs", StringType.INSTANCE)
                .addScalar("xhdg", FloatType.INSTANCE)
                .addScalar("xhgh", StringType.INSTANCE)
                .addScalar("xhfq", StringType.INSTANCE)
                .addScalar("xhgx", StringType.INSTANCE)
                .addScalar("xhph", StringType.INSTANCE)
             .setResultTransformer(Transformers.aliasToBean(StockXiaoku.class));

    }


    /**
     * 获取销库记录数量
     * @return
     */
    public int getXiaokuRecordCount(String key) {
        org.hibernate.Query      query =   em.createNativeQuery(stockXiaoKuRecordCountSql).unwrap(SQLQuery.class) ;

        return (Integer) ( query.setParameter("ps_no","%"+key.trim()+"%").list().get(0));
    }
}
