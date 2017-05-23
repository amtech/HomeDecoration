package com.giants3.hd.server.repository;

import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ErpOrderItemProcess;
import com.giants3.hd.utils.entity.ErpWorkFlow;
import com.giants3.hd.utils.entity.Zhilingdan;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Comparator;
import java.util.List;

/**
 * 从第三方数据库  数据相关
 * 物料采购相关的
 */


public class ErpWorkRepository {

    //流程排序
    private Comparator<ErpOrderItemProcess> comparator    = new Comparator<ErpOrderItemProcess>() {
        @Override
        public int compare(ErpOrderItemProcess o1, ErpOrderItemProcess o2) {
            int o1Index= ErpWorkFlow.findIndexByCode(o1.mrpNo.substring(0,1));
            int o2Index=ErpWorkFlow.findIndexByCode(o2.mrpNo.substring(0,1));
            return o1Index-o2Index;
        }
    };;

    public   String SQL_ZHILINGDAN = "";

    public   String SQL_ORDER_ITEM_PROCESS_BY_ITM = "";


    private EntityManager em;

    public ErpWorkRepository(EntityManager em) {

        this.em = em;
        if (StringUtils.isEmpty(SQL_ZHILINGDAN)) {




                SQL_ZHILINGDAN = SqlScriptHelper.readScript("zhilingdan");

            SQL_ORDER_ITEM_PROCESS_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_itm.sql");


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
                .addScalar("real_prd_name", StringType.INSTANCE)

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

 /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no,int itm) {

        Query query = em.createNativeQuery(SQL_ORDER_ITEM_PROCESS_BY_ITM)

                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        return getErpOrderItemProcesses(query);
    }

    private List<ErpOrderItemProcess> getErpOrderItemProcesses(Query query) {
        List<ErpOrderItemProcess> orders = query.unwrap(SQLQuery.class)
                .addScalar("moDd", StringType.INSTANCE)
                .addScalar("moNo", StringType.INSTANCE)
                .addScalar("prdNo", StringType.INSTANCE)
                .addScalar("mrpNo", StringType.INSTANCE)
                .addScalar("osNo", StringType.INSTANCE)
                .addScalar("idNo", StringType.INSTANCE)
                .addScalar("staDd", StringType.INSTANCE)

                .addScalar("endDd", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("mrpNo", StringType.INSTANCE)

                .addScalar("jgh", StringType.INSTANCE)
                .addScalar("scsx", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItemProcess.class)).list();

        if(orders.size()>0) {

            ArrayUtils.sortList(orders, comparator);
            ErpOrderItemProcess chengping = orders.get(orders.size() - 1);
            String pVersion=StringUtils.spliteId_no(chengping.idNo)[1];
            for (ErpOrderItemProcess process : orders) {
                process.pVersion = pVersion;
                process.orderQty=chengping.qty;
            }

        }
        return orders;
    }




}
