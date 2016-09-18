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

    public int workFlowIndex;
    public String workFlowName;


    /**
     * 订单货款对应的流程数据
     */

    public String workFlowIndexs;
    public String workFlowNames;


    /**
     * 当前状态  1 处理中，2 已发出  3已接受
     */
    public int messageState;


    public String createTimeString;
    public long createTime;
}
