package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 报价数据
 */
@Entity(name="T_Quotation")
public class Quotation  implements Serializable {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    /**
     * 顾客id
     */
    @Basic
    public long customerId  ;





    /**
     * 顾客名称
     */
    @Basic
    public String customerName ="" ;
    /**
     * 报价日期
     */
    @Basic
    public String qDate="";

    /**
     * 报价单号
     */
    @Basic
    public String qNumber="";


    /**
     * 有效日期
     */

    @Basic
    public String vDate="";


    /**
     * 业务员id
     */
    @Basic
    public long  salesmanId;

    /**
     * 业务员
     */
    @Basic
    public String  salesman="";


    /**
     *  币别
     */
    @Basic
    public  String  currency="";


    /**
     * 备注
     */

    @Basic
    public  String  memo="";


    /**
     * 报价类别  普通 咸康
     */
    @Basic
    public long quotationTypeId;

    /**
     * 报价类别  普通 咸康
     */
    @Basic
    public String quotationTypeName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quotation)) return false;

        Quotation quotation = (Quotation) o;

        if (id != quotation.id) return false;
        if (customerId != quotation.customerId) return false;
        if (salesmanId != quotation.salesmanId) return false;
        if (customerName != null ? !customerName.equals(quotation.customerName) : quotation.customerName != null)
            return false;
        if (qDate != null ? !qDate.equals(quotation.qDate) : quotation.qDate != null) return false;
        if (qNumber != null ? !qNumber.equals(quotation.qNumber) : quotation.qNumber != null) return false;
        if (vDate != null ? !vDate.equals(quotation.vDate) : quotation.vDate != null) return false;
        if (salesman != null ? !salesman.equals(quotation.salesman) : quotation.salesman != null) return false;
        if (currency != null ? !currency.equals(quotation.currency) : quotation.currency != null) return false;
        return !(memo != null ? !memo.equals(quotation.memo) : quotation.memo != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (customerId ^ (customerId >>> 32));
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (qDate != null ? qDate.hashCode() : 0);
        result = 31 * result + (qNumber != null ? qNumber.hashCode() : 0);
        result = 31 * result + (vDate != null ? vDate.hashCode() : 0);
        result = 31 * result + (int) (salesmanId ^ (salesmanId >>> 32));
        result = 31 * result + (salesman != null ? salesman.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }
}
