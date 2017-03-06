package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.ConstantData;



/**
 * Created by davidleen29 on 2017/1/18.
 */

public class OrderItemWorkFlowState {

    public long id;


    public long orderItemId;
    public long orderId;
    public String orderName;
    public String productFullName;
    public String photoThumb;
    public String pictureUrl;


    public int workFlowStep;
    public String workFlowName;
    public String workFlowType;//0一级  1 二级（厂家排厂）


    public int nextWorkFlowStep;
    public String nextWorkFlowName;
    public String nextWorkFlowType;//0一级  1 二级（厂家排厂）


    public String productTypeName;
    public String productType;


    /**
     * 加工户id  只有自制才有这个数据
     */
    public String factoryId;
    /**
     * 加工户名称   只有自制才有这个数据
     */
    public String factoryName;


    /**
     * 生产厂家  外购的有这个数据
     */
    public long produceFactory;
    /**
     * 生产厂家名字 外购的有这个数据
     */
    public String produceFactoryName;


    //订单数量
    public int orderQty;

    //当前数量
    public  int qty;

    /**
     * 正在发送中的数量
     */
    public int sendingQty;
    /**
     * 未发送数量
     */
    public int unSendQty;

    public  long  createTime;
    public  String createTimeString;

    public String getMessage() {
        return new StringBuilder().append(workFlowName).append(ConstantData.STRING_DIVIDER_TO).append(productType).append(ConstantData.STRING_DIVIDER_TO).append(factoryName)

                .append(ConstantData.STRING_DIVIDER_TO).append(qty).toString();
    }
}
