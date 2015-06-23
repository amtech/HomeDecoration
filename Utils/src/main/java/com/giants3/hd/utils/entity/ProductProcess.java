package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工序表
 */

@Entity(name="T_ProductProcess")
public class ProductProcess implements Serializable{


    public static final String XISHUA="洗枪洗笔刷费用";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    @Basic
    public String code;


    @Basic
    public String name;


    @Basic
    public String memo;


}
