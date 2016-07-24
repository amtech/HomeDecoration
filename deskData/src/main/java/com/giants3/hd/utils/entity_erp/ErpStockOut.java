package com.giants3.hd.utils.entity_erp;

import java.io.Serializable;

/**
 * Erp 数据库出库数据
 * Created by davidleen29 on 2016/7/16.
 */
public class ErpStockOut   implements Serializable {


    public String ck_no;
    public String ck_dd;
    public String cus_no;
    public String mdg;
    public String tdh;
    public String gsgx;


    //客户地址
    public String adr2;
    //客户电话
    public String tel1;
    //客户传真
    public String fax;




    //本系统增加字段

    public String zhengmai;
    public String cemai;
    public String neheimai;
    public String memo;
    public  String attaches;
}
