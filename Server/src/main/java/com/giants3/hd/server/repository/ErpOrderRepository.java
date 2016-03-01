package com.giants3.hd.server.repository;

import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.entity_erp.Prdt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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


    private EntityManager em;

    public ErpOrderRepository(EntityManager em) {
        this.em = em;
    }


    public List<ErpOrder> findOrders(String os_no, int pageIndex, int pageSize) {

//       long rows= em.createNativeQuery(
//                "SELECT COUNT(c) FROM Country c", Long.class).getSingleResult();

//        em.createNativeQuery(
//                "SELECT COUNT(c) FROM Country c", Long.class).getResultList();
        //long countryCount = query.getSingleResult();
        List result = em.createNativeQuery("select p.os_dd    ,CAST(p.os_no AS varchar) as os_no ,CAST(p.cus_no AS varchar) as cus_no ,CAST(p.cus_os_no AS varchar) as  cus_os_no ,CAST(p.rem AS varchar) as rem ,p.est_dd , p.so_data from  MF_POS p  where p.os_id='so' AND p.os_no like   :os_no  order by p.os_no,p.est_dd DESC " ).setParameter("os_no", "%" + os_no + '%').setFirstResult(pageIndex*pageSize).setMaxResults(pageSize).getResultList();
        return convertToPojo(result);


    }


    public List<ErpOrderItem> findItemsByOrderId(String orderNo ) {





    }

    public List<ErpOrder> convertToPojo(List sqlResult) {

        List<ErpOrder> prdts = new ArrayList<>();
        Iterator it = sqlResult.iterator();

        while (it.hasNext()) {
            Object[] row = (Object[]) it.next(); // Iterating through array object
            // prdts
            ErpOrder order = new ErpOrder();

            // 第一行记录为序号  在分页模式下。
            int i=0;

            //这里解析的顺序跟sql查询语句里 select 先后一致
            i++;
            order.os_dd = row[i] == null ? "" : DateFormats.FORMAT_YYYY_MM_DD.format((Timestamp) row[i]) ;
            i++;
            order.os_no = row[i] == null ? "" : row[i].toString();
            i++;
            order.cus_no = row[i] == null ? "" : row[i].toString();
            i++;
            order.cus_os_no = row[i] == null ? "" : row[i].toString();
            i++;
            order.rem = row[i] == null ? "" : row[i].toString();

//            order.price = row[5] == null ? 0 : Float.valueOf(row[5].toString());

            i++;
            order.est_dd = row[i] == null ? "" : DateFormats.FORMAT_YYYY_MM_DD.format((Timestamp) row[i]) ;
            i++;
            order.so_data = row[i] == null  ? "" : DateFormats.FORMAT_YYYY_MM_DD.format((Timestamp) row[i]) ;

            prdts.add(order);


        }

        return prdts;

    }


}
