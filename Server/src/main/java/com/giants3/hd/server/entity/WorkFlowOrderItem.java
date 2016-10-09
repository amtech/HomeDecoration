package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单货款的进度数据
 * Created by davidleen29 on 2016/9/3.
 */
@Entity(name = "T_WorkFlowOrderItem")
public class WorkFlowOrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    public long orderItemId;
    public String orderName;
    public long orderId;

    public String productName;

    /**
     * 当前进度数据
     */

    public int workFlowStep;
    public String workFlowName;


    /**
     * 订单货款对应的流程数据
     */

    public String workFlowSteps;
    public String workFlowNames;



    /**
     * 当前生产状态  0 正常  1 已发送  2 已接收 3 已审核=0进入下一个流程
     */
    public int workFlowState;


    public String createTimeString;
    public long createTime;

    /**
     * 传递的数量
     */
    public int tranQty;
}
