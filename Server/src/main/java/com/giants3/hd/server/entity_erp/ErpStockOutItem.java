package com.giants3.hd.server.entity_erp;

import java.io.Serializable;

/**
 * Erp 数据库出库d单细项
 * Created by davidleen29 on 2016/7/16.
 */
public class ErpStockOutItem   implements Serializable{

    public String ck_no;
    public int itm;
    public String prd_no;
    public String id_no;
    public String os_no;
    /**
     * 客号
     */
    public String bat_no;
    /**
     * 客户订单号
     */
    public String cus_os_no;


    public float qty;

    /**
     * 单价
     */
    public float up;
    public float amt;

    /**
     * 每箱套数
     */
    public int so_zxs;


    /**
     * 箱数
     */
    public float xs;

    /**
     * 箱规
     */
    public String khxg;

    /**
     * 箱规体积
     */
    public float xgtj;

    /**
     * 总体积
     */

    public  float zxgtj;
    /**
     * 净重
     */
    public float jz1;
    /**
     * 毛重
     */
    public float mz;








    //以下是本系统数据 关联过来

    public String url;
    public String unit;

    /**
     * 版本号， id_no  格式为 13A0760->223311  使用不方便，用 pversion 存放223311
     */
    public String  pVersion;


    //以下是本系统录入数据
    /**
     * 产品描述
     */
    public String describe;

    /**
     * 出库装柜柜号
     */
    public String  guihao;

    /**
     * 封签号
     */
    public String  fengqianhao;
}

