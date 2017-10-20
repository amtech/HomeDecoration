package com.giants3.hd.entity.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 移动端使用的数据 报价
 * Created by david on 2016/1/2.
 */
@Entity(name="T_AppQuotation")
public class Quotation implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

    public String qNumber;



    public String salesman;
    public String vDate;
    public String qDate;



    public String customer;



    /**
     * 是否正式的报价单 即保存过的
     */
    public boolean formal;


    /**
     * 是否打印
     */
    public boolean printed;


}
