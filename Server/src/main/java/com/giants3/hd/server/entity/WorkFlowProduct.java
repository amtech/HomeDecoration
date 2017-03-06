package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 与产品关联的订单流程数据
 * Created by davidleen29 on 2016/12/29.
 */
@Entity(name="T_WorkFlowProduct")
public class WorkFlowProduct  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long productId;
    public String productName;

    public  String productPVersion;


    /**
     * 二级流程配置  铁or木orPU 或者任意组合。 ；隔开
     *
     * 流程类型，如果是细分类型。 1，2，3 形式
     */
    public  String productTypes;
    public  String productTypeNames;


    /**
     * 对应流程step列表  符号；隔开
     */
    public String workFlowSteps;
    /**
     * 对应流程name列表  符号；隔开
     */
    public String workFlowNames;
    /**
     * 对应流程类型列表 0 主流程，1 二级流程  数据形式  1;1;0;0;0;0;0;
     */
    public String  workFlowTypes;



}
