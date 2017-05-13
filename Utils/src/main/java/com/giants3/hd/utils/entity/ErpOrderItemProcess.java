package com.giants3.hd.utils.entity;

/**
 * 订单生产流程
 * Created by davidleen29 on 2017/5/8.
 */
public class ErpOrderItemProcess {

    /**
     * 排产日期

     */
    public String mo_dd;
    /**
    *单号

     */
    public String mo_no;

    /**
     * 预开工日期
     */
    public String sta_dd;

    /**
     * 预完工日期
     */
    public  String end_dd;
    /**
     * 受订单号
     */
    public  String os_no;
    /**
     * EST_ITM
     */
    public  int itm;
    /**
     * MRP_NO
     */
    public  String mrp_no;
    /**
     * MO_NO_ADD
     */
    public String prd_no;
    /**
     * 数量

     */
    public int qty;

    /**
     * 生产厂家
     */
    public String jgh;
    /**
     * 生产属性
     */
    public String scsx;

}
