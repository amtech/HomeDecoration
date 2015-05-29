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
     * 顾客名称
     */
    @Basic
    public String name  ;


}
