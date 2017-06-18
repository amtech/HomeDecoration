package com.giants3.hd.server.repository;

import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.utils.entity.ErpOrderItemProcess;
import com.giants3.hd.utils.entity.ErpWorkFlow;
import com.giants3.hd.utils.entity.Zhilingdan;
import com.giants3.hd.utils.entity_erp.ErpWorkFlowOrderItem;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 从第三方数据库  数据相关
 * 物料采购相关的
 */


public class ErpWorkRepository  extends ErpRepository{

    //流程排序
    private Comparator<ErpOrderItemProcess> comparator = new Comparator<ErpOrderItemProcess>() {
        @Override
        public int compare(ErpOrderItemProcess o1, ErpOrderItemProcess o2) {
            int o1Index = ErpWorkFlow.findIndexByCode(o1.mrpNo.substring(0, 1));
            int o2Index = ErpWorkFlow.findIndexByCode(o2.mrpNo.substring(0, 1));
            return o1Index - o2Index;
        }
    };
    ;

    public String SQL_ZHILINGDAN = "";

    public String SQL_ORDER_ITEM_PROCESS_BY_ITM = "";
    public String SQL_ORDER_ITEM_UNCOMPLETE = "";


    private EntityManager em;

    public ErpWorkRepository(EntityManager em) {

        this.em = em;
        if (StringUtils.isEmpty(SQL_ZHILINGDAN)) {


            SQL_ZHILINGDAN = SqlScriptHelper.readScript("zhilingdan");

            SQL_ORDER_ITEM_PROCESS_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_itm.sql");
            SQL_ORDER_ITEM_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);

        }
    }


    /**
     * 查询指令单完成状态表
     *
     * @param osName
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        Query query = em.createNativeQuery(SQL_ZHILINGDAN)
                .setParameter("osname", "%" + osName.trim() + "%")
                .setParameter("startdate", startDate)
                .setParameter("enddate", endDate);
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
                .setResultTransformer(Transformers.aliasToBean(Zhilingdan.class)).list();


        return orders;
    }

    /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no, int itm) {

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

        if (orders.size() > 0) {


            //模拟第一个流程的数据。     其实就是白胚体数据
            {


                List<ErpOrderItemProcess> peitis = new ArrayList<>();

                ErpOrderItemProcess yanse = null;

                List<ErpOrderItemProcess> firstSteps = new ArrayList<>();
                for (ErpOrderItemProcess process : orders) {
                    if (process.mrpNo.startsWith(ErpWorkFlow.SECOND_STEP_CODE)) {
                        final ErpOrderItemProcess object = process;
                        ErpOrderItemProcess first = ObjectUtils.deepCopyWidthJson(object, ErpOrderItemProcess.class);
                        first.mrpNo = ErpWorkFlow.FIRST_STEP_CODE + first.mrpNo.substring(1);
                        firstSteps.add(first);
                        peitis.add(process);
                    }

                    if (process.mrpNo.startsWith(ErpWorkFlow.CODE_YANSE)) {
                        yanse = process;
                    }

                }

                orders.addAll(firstSteps);


                //颜色流程根据胚体流程同步（有铁木就拆铁木）


                int size = peitis.size();
                List<ErpOrderItemProcess> yanses = new ArrayList<>();
                if (size > 1) {
                    for (int i = 0; i < size - 1; i++) {
                        yanses.add(ObjectUtils.deepCopyWidthJson(yanse, ErpOrderItemProcess.class));
                    }
                }


                if (yanses.size() > 0) {
                    orders.remove(yanse);
                    yanses.add(yanse);
                    for (int i = 0; i < size; i++) {
                        ErpOrderItemProcess temp = yanses.get(i);

                        String type = peitis.get(i).mrpNo.substring(1, 2);
                        temp.mrpNo = temp.mrpNo.substring(0, 1) + type + temp.mrpNo.substring(1);

                    }
                    orders.addAll(yanses);
                }


            }


            //移除组装流程

            List<ErpOrderItemProcess> temp = new ArrayList<>();
            for (ErpOrderItemProcess process : orders) {

                if (process.mrpNo.startsWith(ErpWorkFlow.CODE_ZUZHUANG)) {
                    temp.add(process);
                }
            }
            orders.removeAll(temp);


            ArrayUtils.sortList(orders, comparator);
            ErpOrderItemProcess chengping = orders.get(orders.size() - 1);

            //成品仓数据
            chengping.mrpNo = ErpWorkFlow.CODE_CHENGPIN + chengping.mrpNo;


            String pVersion = StringUtils.spliteId_no(chengping.idNo)[1];
            for (ErpOrderItemProcess process : orders) {
                process.pVersion = pVersion;
                process.orderQty = chengping.qty;
            }


        }
        return orders;
    }


    /**
     * 查找已排产未出货的订单列表
     *
     * @param key
     * @return
     */
    public List<ErpWorkFlowOrderItem> searchUnCompleteOrderItems(String key) {

        final String value = StringUtils.sqlLike(key);
        Query query = em.createNativeQuery(SQL_ORDER_ITEM_UNCOMPLETE)
                .setParameter("os_no", value)
                .setParameter("prd_no", value);
        final List<ErpWorkFlowOrderItem> workFlowOrderItemListQuery = getWorkFlowOrderItemListQuery(query);


        for(ErpWorkFlowOrderItem item:workFlowOrderItemListQuery){

            item.pversion=StringUtils.spliteId_no(item.id_no)[1];
        }
        return workFlowOrderItemListQuery;


    }



    private  List<ErpWorkFlowOrderItem> getWorkFlowOrderItemListQuery(Query query)
    {


        return  query .unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("workFlowState", IntegerType.INSTANCE)
                .addScalar("workFlowDescribe", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpWorkFlowOrderItem.class)).list();

    }
}
