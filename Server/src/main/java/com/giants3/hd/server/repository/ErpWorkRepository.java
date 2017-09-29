package com.giants3.hd.server.repository;

import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpOrderItemProcess;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.server.utils.SqlScriptHelper;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
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


public class ErpWorkRepository extends ErpRepository {

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
    public String SQL_WORK_FLOW_MATERIAL = "";
    public String SQL_ORDER_ITEM_BY_OS_ITM = "";

    public String SQL_ORDER_ITEM_PROCESS_BY_ITM = "";
    public String SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM = "";
    public String SQL_ORDER_ITEM_UNCOMPLETE = "";
    public String SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE = "";


    private EntityManager em;

    public ErpWorkRepository(EntityManager em) {

        this.em = em;
        if (StringUtils.isEmpty(SQL_ZHILINGDAN)) {


            SQL_ZHILINGDAN = SqlScriptHelper.readScript("zhilingdan");
            SQL_WORK_FLOW_MATERIAL = SqlScriptHelper.readScript("work_flow_material.sql");

            SQL_ORDER_ITEM_PROCESS_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_itm.sql");
            SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM = SqlScriptHelper.readScript("orderItemProcess_purchase_itm.sql");
            SQL_ORDER_ITEM_BY_OS_ITM = SqlScriptHelper.readScript("FindErpOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_ORDER_ITEM_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_COMPLETE);
            SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE = SqlScriptHelper.readScript("unCompleteAndStartWorkOrderItem.sql").replace("VALUE_COMPLETE_STATE", "" + ErpWorkFlow.STATE_WORKING);
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
     * 查询采购单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findPurchaseOrderItemProcesses(String os_no, int itm) {



        Query query = em.createNativeQuery(SQL_ORDER_ITEM_PROCESS_PURCHASE_BY_ITM)

                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        final List<ErpOrderItemProcess> orderItemProcesses = extractOrderItemProcessFromQuery(query);


        for (ErpOrderItemProcess process : orderItemProcesses) {

            String url = com.giants3.hd.server.utils.FileUtils.getErpProductPictureUrl(process.idNo, "");

            String pVersion = StringUtils.spliteId_no(process.idNo)[1];
            process.pVersion = pVersion;
            process.orderQty = process.qty;
            process.photoUrl = url;
            process.photoThumb = url;
        }

        return orderItemProcesses;


    }

    /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no, int itm) {

        return findOrderItemProcesses(os_no, itm, false);
    }

    /**
     * 查询指令单完成状态表
     *
     * @param os_no
     * @param itm
     * @param includeZuzhuang 是否包含组装流程数据
     * @return
     */
    public List<ErpOrderItemProcess> findOrderItemProcesses(String os_no, int itm, boolean includeZuzhuang) {

        Query query = em.createNativeQuery(SQL_ORDER_ITEM_PROCESS_BY_ITM)

                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        return getErpOrderItemProcesses(query, includeZuzhuang);
    }

    private List<ErpOrderItemProcess> getErpOrderItemProcesses(Query query, boolean includeZuzhuang) {
        List<ErpOrderItemProcess> orders = extractOrderItemProcessFromQuery(query);

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


            List<ErpOrderItemProcess> zuzhuangTemp = new ArrayList<>();
            for (ErpOrderItemProcess process : orders) {

                if (process.mrpNo.startsWith(ErpWorkFlow.CODE_ZUZHUANG)) {
                    zuzhuangTemp.add(process);
                }

                if (process.mrpNo.startsWith(ErpWorkFlow.CODE_BAOZHUANG)) {
                    //包装流程， qty单位以箱   x每个箱装数， 成为正确的排厂数量
                    process.qty = process.qty * process.so_zxs;
                }
            }
            orders.removeAll(zuzhuangTemp);


            ArrayUtils.sortList(orders, comparator);
            ErpOrderItemProcess chengping = orders.get(orders.size() - 1);

            //成品仓数据
            chengping.mrpNo = ErpWorkFlow.CODE_CHENGPIN + chengping.mrpNo;


            String url = com.giants3.hd.server.utils.FileUtils.getErpProductPictureUrl(chengping.idNo, "");

            String pVersion = StringUtils.spliteId_no(chengping.idNo)[1];
            for (ErpOrderItemProcess process : orders) {
                process.pVersion = pVersion;
                process.orderQty = chengping.qty;
                process.photoUrl = url;
                process.photoThumb = url;
            }


            //需要组装数据 重新添加上。只有在配置流程名称时候需要
            //有组装 包装流程名称为  组装包装， 否则就叫做包装
            if (includeZuzhuang) {
                orders.addAll(zuzhuangTemp);

            }

        }
        return orders;
    }

    private List<ErpOrderItemProcess> extractOrderItemProcessFromQuery(Query query) {
        return query.unwrap(SQLQuery.class)
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
                    .addScalar("so_zxs", IntegerType.INSTANCE)

                    .addScalar("jgh", StringType.INSTANCE)
                    .addScalar("scsx", StringType.INSTANCE)
                    .addScalar("qty", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(ErpOrderItemProcess.class)).list();
    }


    /**
     * 查找已排产未出货的订单列表
     *
     * @param key
     * @return
     */
    public List<ErpOrderItem> searchUnCompleteOrderItems(String key) {


        return getOrderItemsFromSQL(SQL_ORDER_ITEM_UNCOMPLETE, key);


    }




    private List<ErpOrderItem> getWorkFlowOrderItemListQuery(Query query) {


        //增加字段 up

        return query.unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("bat_no", StringType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("id_no", StringType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("photoUpdateTime", IntegerType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)
                .addScalar("up", FloatType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("hpgg", StringType.INSTANCE)
                .addScalar("khxg", StringType.INSTANCE)
                .addScalar("so_zxs", IntegerType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("os_dd", StringType.INSTANCE)
                .addScalar("amt", FloatType.INSTANCE)
                .addScalar("produceType", IntegerType.INSTANCE)
                .addScalar("workFlowState", IntegerType.INSTANCE)
                .addScalar("maxWorkFlowStep", IntegerType.INSTANCE)
                .addScalar("maxWorkFlowName", StringType.INSTANCE)
                .addScalar("maxWorkFlowCode", StringType.INSTANCE)
                .addScalar("workFlowDescribe", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItem.class)).list();

    }

    public List<WorkFlowMaterial> searchWorkFlowMaterials(String osNo, int itm, String code) {


        Query query = em.createNativeQuery(SQL_WORK_FLOW_MATERIAL)
                .setParameter("os_no", osNo)
                .setParameter("itm", itm)
                .setParameter("mrp_no", StringUtils.sqlRightLike(code));


        final List<WorkFlowMaterial> list = getList(query);

        for (WorkFlowMaterial material : list) {

            material.mo_dd = StringUtils.clipSqlDateData(material.mo_dd);
        }
        return list;


    }


    private List<WorkFlowMaterial> getList(Query query) {


        return query.unwrap(SQLQuery.class)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("itm", IntegerType.INSTANCE)
                .addScalar("prd_no", StringType.INSTANCE)
                .addScalar("prd_name", StringType.INSTANCE)
                .addScalar("prd_mark", StringType.INSTANCE)
                .addScalar("mrp_no", StringType.INSTANCE)
                .addScalar("mo_no", StringType.INSTANCE)
                .addScalar("mo_dd", StringType.INSTANCE)
                .addScalar("qty_rsv", IntegerType.INSTANCE)
                .addScalar("qty", IntegerType.INSTANCE)
                .addScalar("qty_std", FloatType.INSTANCE)
                .addScalar("real_prd_name", StringType.INSTANCE)
                .addScalar("rem", StringType.INSTANCE)
                .addScalar("ut", StringType.INSTANCE)

                .setResultTransformer(Transformers.aliasToBean(WorkFlowMaterial.class)).list();

    }


    public List<ErpOrderItem> searchHasStartWorkFlowUnCompleteOrderItems(String key) {


        return getOrderItemsFromSQL(SQL_ORDER_ITEM_HAS_START_AND_UNCOMPLETE, key);

    }


    private List<ErpOrderItem> getOrderItemsFromSQL(String sql, String key) {

        final String value = StringUtils.sqlLike(key);
        Query query = em.createNativeQuery(sql)
                .setParameter("os_no", value)
                .setParameter("prd_no", value);
        return getORderItemsFromSQL(query);
//        final List<ErpOrderItem> workFlowOrderItemListQuery = getWorkFlowOrderItemListQuery(query);
//
//
//        for (ErpOrderItem item : workFlowOrderItemListQuery) {
//
//            item.pVersion = StringUtils.spliteId_no(item.id_no)[1];
//            item.so_data=StringUtils.clipSqlDateData(item.so_data);
//            item.os_dd=StringUtils.clipSqlDateData(item.os_dd);
//
//            switch (item.produceType)
//            {
//                case ProduceType.NOT_SET:
//                    item.produceTypeName=ProduceType.NOT_SET_NAME;
//                    break;
//                case ProduceType.SELF_MADE:
//                    item.produceTypeName=ProduceType.SELF_MADE_NAME;
//                    break;
//                case ProduceType.PURCHASE:
//                    item.produceTypeName=ProduceType.PURCHASE_NAME;
//                    break;
//            }
//
//        }
//        return workFlowOrderItemListQuery;

    }


    private List<ErpOrderItem> getORderItemsFromSQL(Query query) {
        final List<ErpOrderItem> workFlowOrderItemListQuery = getWorkFlowOrderItemListQuery(query);


        for (ErpOrderItem item : workFlowOrderItemListQuery) {

            item.pVersion = StringUtils.spliteId_no(item.id_no)[1];
            item.so_data = StringUtils.clipSqlDateData(item.so_data);
            item.os_dd = StringUtils.clipSqlDateData(item.os_dd);

            switch (item.produceType) {
                case ProduceType.NOT_SET:
                    item.produceTypeName = ProduceType.NOT_SET_NAME;
                    break;
                case ProduceType.SELF_MADE:
                    item.produceTypeName = ProduceType.SELF_MADE_NAME;
                    break;
                case ProduceType.PURCHASE:
                    item.produceTypeName = ProduceType.PURCHASE_NAME;
                    break;
            }

        }
        return workFlowOrderItemListQuery;
    }


    /**
     *  查找订单数据
     * @param os_no
     * @param itm
     * @return
     */
    public ErpOrderItem findOrderItem(String os_no, int itm) {


        Query query = em.createNativeQuery(SQL_ORDER_ITEM_BY_OS_ITM)
                .setParameter("os_no", os_no)
                .setParameter("itm", itm);
        final List<ErpOrderItem> oRderItemsFromSQL = getORderItemsFromSQL(query);
        if (oRderItemsFromSQL != null && oRderItemsFromSQL.size() > 0) return oRderItemsFromSQL.get(0);
        return null;
    }
}