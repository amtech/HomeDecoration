package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单货款的生产进度数据
 * Created by davidleen29 on 2017/1/1.
 */
@Entity(name="T_OrderItemWorkState")
public class OrderItemWorkState {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    public String osNo;
    public int itm;

    /**
     *产品进度描述
     */
    public String workFlowDescribe;




    /**
     * 订单状态 {@link ErpWorkFlow#STATE_COMPLETE}
     * 订单状态 {@link ErpWorkFlow#STATE_WORKING}
     */
    public int workFlowState;


    /**
     * 当前执行到的流程序号
     */
    public int maxWorkFlowStep;
    public String maxWorkFlowName;

    public String maxWorkFlowCode;


    public String url;

    public String prdNo;

}
