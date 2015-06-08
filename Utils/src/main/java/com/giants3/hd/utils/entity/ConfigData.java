package com.giants3.hd.utils.entity;

/**
 * 配置数据  提供一些常量值
 */

public class ConfigData {


    /**
     * 是稀释剂单价
     */
    public float price_of_diluent=8.3f;


    /**
     * 汇率比值
     */
    public  float exportRate=6;

    /**
     * 成本利润比值。
     */

    public float cost_price_ratio=0.65f;

    /**
     * 附加值
     *
     */

    public float  addition=0.15f;


    /**
     *出口运费   元/M3
     */

    public float price_of_export=95;
    private static ConfigData instance=new ConfigData();

    public static ConfigData getInstance() {
        return instance;
    }
}
