package com.giants3.hd.server.entity;

import com.giants3.hd.server.converter.StringMessageConverter;

/**
 *
 *订单货款的进度数据
 * Created by davidleen29 on 2016/9/3.
 */
public class WorkFlowOrderItem {


    public long orderItemId;
    public  String orderName;
    public String orderId;
    public long productId;
    public String productName;

    /**
     * 当前进度数据
     */
    public long processFlowId;
    public String processFlowName;


    /**
     * 订单货款对应的流程数据
     */
    public String processFlowIds;
    public String processFlowNames;

    //是否自动审核
    public String  autoChecks;




    /**
     * 当前状态  1 处理中，2 已发出  3已接受
     */
    public int messageState;





}
