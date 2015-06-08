package com.giants3.hd.utils.entity;

import javax.persistence.*;

/**
 * 材料分类  根据编码头四位进行区分
 */
@Entity(name="T_MaterialClass")
public class MaterialClass {


    /**
     *
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    /**
     * 编码头四位（不含C）
     */
    @Basic
    public String code;

    @Basic
    public String name;


    @Override
    public String toString() {
        return  "  ["+code+"]     "+name;
    }
}
