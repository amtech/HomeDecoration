package com.giants3.hd.server.repository;

import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
import com.giants3.hd.server.entity_erp.Prdt;
import com.giants3.hd.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 从第三方数据库读取erpOrder数据相关 订单
 */
public class ErpOrderRepository {


    public static final String KEY_ORDER = "SO";
    public static final String OS_ID = "OS_ID";
    public static final String KEY_OS_NO = "OS_NO";
    private EntityManager em;

    /**
     * AS varchar(8000)   在sqlserver 2000 中  最大的varchar 长度为8000 varchar(max) 会报错。
     */
    //模糊查找  os_no like  ：os_no
    public static final String SQL_ORDER_LIST_SEARCH = "select  a.os_dd,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,b.so_data from  (select p.os_dd    ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO)  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO like :OS_NO )   b on a.os_no=b.os_no   order by a.est_dd DESC  ";

    //精确查找， os_no=  ：os_no
    public static final String SQL_ORDER_LIST_FIND = "select  a.os_dd,a.os_no,a.cus_no,a.cus_os_no , a.sal_no ,a.rem,a.est_dd,b.so_data from  (select p.os_dd    ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no , CAST (p.sal_no as VARCHAR ) as sal_no , CAST(p.rem AS varchar(8000)) as rem ,p.est_dd  from  MF_POS p where p.OS_ID=:OS_ID and p.OS_NO = :OS_NO)  a  LEFT JOIN\n" +
            "  \n" +
            "(select pz.os_id,pz.os_no,pz.so_data from  MF_POS_Z pz where pz.OS_ID=:OS_ID and pz.OS_NO = :OS_NO )   b on a.os_no=b.os_no   order by a.est_dd DESC  ";

    public static final String SQL_ORDER_LIST_COUNT = " select  count(p.os_no)  from  MF_POS p where p.OS_ID=:OS_ID and p.OS_NO like :OS_NO  ";

    public static String sql_item_1 = "   select os_no,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt from tf_pos  where os_id=:OS_ID and os_no =:OS_NO ";
    public static String sql_item_2 = " select isnull(htxs,0) as htxs,isnull(so_zxs,0) as so_zxs ,khxg,isnull(xgtj,0) as xgtj , isnull(zxgtj,0) as zxgtj ,hpgg ,os_no,itm from tf_pos_z    where os_id=:OS_ID and os_no =:OS_NO ";
    public static final String SQL_ORDER_ITEM_LIST = "  select  a.* ,b.* from (  " + sql_item_1 + ") a left outer join (" + sql_item_2 + ") b on a.os_no=b.os_no and a.itm=b.itm order by a.os_no ,a.itm asc";

    public ErpOrderRepository(EntityManager em) {
        this.em = em;
    }


    /*
    *   查询订单
    *   返回2个元素数组    【0】 表示查询记录数
    *       【1】 表示查询的结果列表  List<ErpOrder>
     */
    public List<ErpOrder> findOrders(String os_no, int pageIndex, int pageSize) {



        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_SEARCH).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + os_no + '%').setFirstResult(pageIndex * pageSize).setMaxResults(pageSize).list();


        modifyDateString(orders);

        return orders;


    }

    /**
     * 获取关键字查询记录总数
     *
     * @param key
     * @return
     */
    public int getOrderCountByKey(String key) {
        return (Integer) em.createNativeQuery(SQL_ORDER_LIST_COUNT).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, "%" + key + '%').getSingleResult();
    }





    private org.hibernate.Query getOrderListQuery(String sql)
    {


        Query query = em.createNativeQuery(sql) ;
       return query.unwrap(SQLQuery.class).addScalar("os_dd", StringType.INSTANCE)
                .addScalar("os_no", StringType.INSTANCE)
                .addScalar("cus_no", StringType.INSTANCE)
                .addScalar("cus_os_no", StringType.INSTANCE)
                .addScalar("rem", StringType.INSTANCE)
                .addScalar("est_dd", StringType.INSTANCE)
                .addScalar("sal_no", StringType.INSTANCE)
                .addScalar("so_data", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ErpOrder.class));


    }

    /**
     * 调整日期数据
     * @param data
     */
    protected void modifyDateString(List<ErpOrder> data)
    {
        if(data==null) return ;
        for(ErpOrder item:data)
        {

            //时间相关 取前10
            if(!StringUtils.isEmpty(item.os_dd)) {
                item.os_dd=item.os_dd.trim();
                if (item.os_dd.length() > 10) {
                    item.os_dd=item.os_dd.substring(0, 10);
                }
            }

            if(!StringUtils.isEmpty(item.est_dd)) {
                item.est_dd=item.est_dd.trim();
                if (item.est_dd.length() > 10) {
                    item.est_dd=item.est_dd.substring(0, 10);
                }
            }

            if(!StringUtils.isEmpty(item.so_data)) {
                item.so_data=item.so_data.trim();
                if (item.so_data.length() > 10) {
                    item.so_data=item.so_data.substring(0, 10);
                }
            }
        }
    }
    /**
     * 查看订单item
     *
     * @param orderNo
     * @return
     */
    public List<ErpOrderItem> findItemsByOrderNo(String orderNo) {


        List<ErpOrderItem> result = em.createNativeQuery(SQL_ORDER_ITEM_LIST).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO, orderNo)
                .unwrap(SQLQuery.class)
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
                .setResultTransformer(Transformers.aliasToBean(ErpOrderItem.class)).list();







        return result;


    }


    /**
     * 精确查询订单
     * @param os_no
     * @return
     */
    public ErpOrder findOrderByNO(String os_no) {


        List<ErpOrder> orders = getOrderListQuery(SQL_ORDER_LIST_FIND).setParameter(OS_ID, KEY_ORDER).setParameter(KEY_OS_NO,   os_no  ).list();

                if(orders!=null&&orders.size()>0)
                {

                    modifyDateString(orders);
                    ErpOrder order=orders.get(0);
                    return order;

                }


        return null;
    }
}
