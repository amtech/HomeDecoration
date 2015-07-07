package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity_erp.CST_STD;
import com.giants3.hd.utils.entity_erp.Prdt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
*
*/


public   class ErpPrdtRepository    {





    private EntityManager em;
    public   ErpPrdtRepository(EntityManager em) {

        this.em=em;
    }




    //@Query(value = " SELECT  a    FROM PRDT a WHERE  a.prd_no like  :prd_no      " )
    public   List<Prdt> findByPrd_noEquals(@Param("prd_no") String prd_no,Pageable pageable)
    {

       CriteriaBuilder builder = em.getCriteriaBuilder();
//
//
//        CriteriaQuery<Prdt> query = builder.createQuery(Prdt.class);
//        Root<Prdt> root = query.from(Prdt.class);
//
//        Join<Prdt, CST_STD>  joinTable=    root.join(CST_STD.class.getSimpleName(), JoinType.INNER);
//        joinTable.
//
//
//        Predicate hasBirthday = builder.equal(root.get("prd_no"), prd_no);
//        //Predicate isLongTermCustomer = builder.lessThan(root.get(Customer_.createdAt), today.minusYears(2);
//        query.where(builder.and(hasBirthday));//, isLongTermCustomer
//     return   em.createQuery(query.select(root)).getResultList();

// where e.prd_no = :prd_no   .setParameter(p,prd_no)
       // return em.createQuery("select  e  from PRDT e left join CST_STD  d fetch    e.prd_no = d.prd_no where e.prd_no=:prd_no ",Prdt.class) .setParameter("prd_no",prd_no).getResultList();
//        Specification<Prdt> prdtSpecification=new Prdt
//        getQuery()
//        TypedQuery query = em.createQuery("select a from Account a where a.customer = ?1", Account.class);
//        query.setParameter(1, customer);


     List result=   em.createNativeQuery("select CAST(p.prd_no AS varchar) as prd_no ,CAST(p.name AS varchar) as name ,CAST(p.ut AS varchar) as ut ,CAST(p.spc AS varchar) as  spec ,CAST(p.rem AS varchar) as rem ,cs.price from (select * from  prdt  where prdt.knd='4') p inner join (select prd_no , up_std as price from CST_STD ) cs on p.prd_no=cs.prd_no  where p.prd_no = :prd_no " ).setParameter("prd_no",prd_no).getResultList();
     ///   return em.createQuery("select  e  from prdt e  ,(select f.prd_no,f.up_std from CST_STD f) d where e.prd_no=:prd_no ",Prdt.class) .setParameter("prd_no",prd_no).getResultList();
        return  convertToPojo(result);


    }




    public   List<Prdt> list( )
    {


        //找到唯一的单价记录    利用日期 最大值判断
        String sql_find_distinct_cst_std="select a.prd_no,a.UP_CST_ML as price from CST_STD a  inner join  (select distinct  prd_no ,MAX(SYS_DATE) as sys_date  from CST_STD  group by PRD_NO ) c on c.PRD_NO =a.PRD_NO  and c.sys_date=a.sys_date where UP_CST_ML>0  ";



        List result=   em.createNativeQuery("select CAST(p.prd_no AS varchar) as prd_no ,CAST(p.name AS varchar) as name ,CAST(p.ut AS varchar) as ut ,CAST(p.spc AS varchar) as  spec ,CAST(p.rem AS varchar) as rem ,cs.price from (select * from  prdt  where prdt.knd='4') p inner join ("+sql_find_distinct_cst_std+") cs on p.prd_no=cs.prd_no  " ).getResultList();


      return  convertToPojo(result);



    }

    public  List<Prdt> convertToPojo(List sqlResult)
    {

        List<Prdt> prdts=new ArrayList<>();
        Iterator it = sqlResult.iterator( );

        while (it.hasNext( )) {
            Object[] row = (Object[])it.next(); // Iterating through array object
            // prdts
            Prdt prdt=new Prdt();

            prdt.prd_no=row[0]==null?"":row[0].toString();

            prdt.name=row[1]==null?"" : row[1].toString();

            prdt.ut=row[2]==null?"":row[2].toString();

            prdt.spec=row[3]==null?"":row[3].toString();

            prdt.rem=row[4]==null?"":row[4].toString();

            prdt.price=row[5]==null?0: Float.valueOf(row[5].toString());

            prdts.add(prdt);


        }

        return prdts;

    }

}
