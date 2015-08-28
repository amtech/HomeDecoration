package com.giants3.hd.utils.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 配置数据  提供一些常量值
 */

@Entity(name="T_GlobalData")
public class GlobalData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    /**
     * 是稀释剂单价
     */
    public float price_of_diluent=9f;


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
     private static GlobalData instance=new GlobalData();


    /**
     * 稀释剂冗余量， 用为洗刷枪笔
     */
    public float extra_ratio_of_diluent=0.1f;

    public static GlobalData getInstance() {
        return instance;
    }



}
