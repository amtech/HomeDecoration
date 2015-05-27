package com.giants3.hd.utils.entity;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 流程基本数据  基本为四个流程  1   白胚   2 油漆  3组装  4  包装
 */

public class Flow {


    /**
     * 流程id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    /**
     * 流程名称
     */
    @Basic
    public String name;



    public static final int FLOW_CONCEPTUS=1;
    public static final int FLOW_ASSEMBLE=2;
    public static final int FLOW_PAINT=3;
    public static final int FLOW_PACK=4;
}
