package com.giants3.hd.utils.entity_erp;

/**
 *
 * erp 订单货款信息
 *
 * table ：TF_POS   TF_POS_Z
 * Created by david on 2016/2/28.
 */

public class ErpOrderItem {

    /**
     * 客号
     */
    public String bat_no;

    /**
     * 货号
     */
    public String prd_no;
    /**
     * 配方号
     */
    public String id_no;

    /**
     * 单位
     */
    public String ut;

    /**
     * 单价
     */
    public String up;


    /**
     * 数量
     */
    public int qty;
    /**
     * 金额
     */
    public float amt;

    /**
     * 箱数
     * TF_POS_Z
     */
    public int htxs;

    /**
     * 每箱数
     * TF_POS_Z
     */
    public int so_zxs;
    /**
     * 箱规
     * TF_POS_Z
     */
    public String khxg;
    /**
     * 立方数
     * TF_POS_Z
     */
    public String zxgtj;
    /**
     * 产品尺寸
     * TF_POS_Z
     */
    public String hpgg;
}
