package com.giants3.hd.utils.entity;

import javax.persistence.*;

/**
 * 报价明细列表
 */
@Entity(name="T_QuotationItem")
public class QuotationItem {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    /**
     *
     */
    @Basic
    public long productId=-1;


    /**
     *
     */
    @Basic
    public String productName;

    @Lob  @Basic
    public byte[]  productPhoto;


    @Basic
    public String pVersion;

    @Basic
    public int  inBoxCount;


    /**
     * 包装箱数
     */
    @Basic
    public int packQuantity;


    /**
     * 箱子规格
     */
    @Basic
    public String packageSize;


    /**
     * 单位
     */
    @Basic
    public String unit;

    /**
     * 成本价
     */
    @Basic
    public float cost;


    /**
     * 单价
     */
    @Basic
    public float price;

    /**
     * 立方数
     */
    @Basic
    public float volumeSize;
    /**
     *净重
     */
    @Basic
    public float weight;



    /**
     *货品规格
     */
    @Basic
    public String spec;



    /**
     *材质
     */
    @Basic
    public String constitute;


    /**
     *镜面尺寸
     */
    @Basic
    public String mirrorSize;

    public void updateProduct(Product product) {


        productPhoto=product.photo;
        productName=product.name;
        pVersion=product.pVersion;
        inBoxCount=product.insideBoxQuantity;
        packQuantity=product.packQuantity;
        packageSize=product.packLong+"*"+product.packWidth+"*" +product.packHeight;

        unit=product.pUnitName;
        cost=product.cost;
        price=product.price;
        volumeSize=product.getPackVolume();
        weight=product.weight;
        spec=product.spec;
        constitute=product.constitute;
        mirrorSize=product.mirrorSize;


    }
}
