package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 报价数据
 */
@Entity(name="T_Quotation")
public class Quotation  {


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
    public String customerName  ;
    /**
     * 报价日期
     */
    @Basic
    public String qDate;

    /**
     * 报价单号
     */
    @Basic
    public String qNumber;


    /**
     * 有效日期
     */

    @Basic
    public String vDate;


    /**
     * 业务员
     */


    @Basic
    public String  salesman;


    /**
     *  币别
     */
    @Basic
    public  String  currency;


    /**
     * 备注
     */

    @Basic
    public  String  memo;

}
