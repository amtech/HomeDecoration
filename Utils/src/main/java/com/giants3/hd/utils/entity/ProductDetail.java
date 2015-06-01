package com.giants3.hd.utils.entity;

import java.util.List;

/**
 * 产品详细信息
 */
public class ProductDetail  {


    public Product product;
    /**
     * 白胚材料列表
     */
    public List<ProductMaterial> conceptusMaterials;
    /**
     * 组装材料列表
     */
    public List<ProductMaterial> assembleMaterials;

    /**
     * 油漆材料工序列表
     */
    public  List<ProductPaint> paints;


    /**
     * 白胚工资列表
     */

    public List<ProductWage> conceptusWages;
    /**
     * 组装工资列表
     */
    public List<ProductWage> assembleWages;
    /**
     *
     *
     * 包装材料列表
     */
    public List<ProductMaterial> packMaterials;
    /**
     * 包装工资列表
     */
    public List<ProductWage> packWages;
}
