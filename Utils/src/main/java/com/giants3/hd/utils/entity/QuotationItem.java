package com.giants3.hd.utils.entity;

import javax.persistence.*;

/**
 * 报价明细列表
 */
@Entity(name="T_QuotationList")
public class QuotationItem {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    /**
     *
     */
    @Basic
    public long productId=-1;


    /**
     *
     */
    @Basic
    public String productname;

}
