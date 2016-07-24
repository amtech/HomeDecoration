package com.giants3.hd.server.entity_erp;

import java.io.Serializable;

/**
 * Erp 数据库出库d单细项
 * Created by davidleen29 on 2016/7/16.
 */
public class ErpStockOutItem   implements Serializable {

    public String ck_no;
    public int itm;
    public String prd_no;
    public String id_no;
    public String os_no;
    public String bat_no;
    public String cus_os_no;


    public float qty;
    public float up;

    public float amt;


    public String so_zxs;


    public String xs;

    public String khxg;

    public String xgtj;

    public float jz1;

    public float mz;






    //以下是本系统数据 关联过来


    public byte[] photo;
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
