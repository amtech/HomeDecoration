package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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


    public String name;

}
