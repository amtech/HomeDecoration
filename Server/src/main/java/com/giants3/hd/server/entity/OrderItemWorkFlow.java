package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单货款的生产进度数据
 * Created by davidleen29 on 2017/1/1.
 */
@Entity(name="T_OrderItemWorkFlow")
public class OrderItemWorkFlow {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    public long orderItemId;
    public long orderId;
    public String orderName;
    public int orderItemIndex;

    String bat_no;




    public String workFlowIds;
    public String workFlowNames;
    /**
     * 流程类型 0 主流程，1 二级流程
     */
    public String  workFlowTypes;

    /**
     * 二级流程配置  铁or木orPU 或者任意组合。 ；隔开
     * <p/>
     * 流程类型，如果是细分类型。 1，2，3 形式
     */
    public  String productTypes;
    public  String productTypeNames;


    /**
     *产品类型对应的厂家ids
     */
    public String productFactoryIds;
    /**
     * 产品类型对应的厂家名称
     */
    public String productFactoryNames;



    /**
     *产品进度描述
     */
    public String workFlowDiscribe;
}
