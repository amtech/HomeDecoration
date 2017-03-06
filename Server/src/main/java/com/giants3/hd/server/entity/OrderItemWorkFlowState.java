package com.giants3.hd.server.entity;

import com.giants3.hd.utils.ConstantData;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2017/1/18.
 */

@Entity(name="T_OrderItemWorkFlowState")
public class OrderItemWorkFlowState {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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



    public String factoryId;
    public String factoryName;
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


    /**
     *    已经发送数量
     */
    public int  sentQty;

    public  long  createTime;
    public  String createTimeString;

    public String getMessage() {
        return new StringBuilder().append(workFlowName).append(ConstantData.STRING_DIVIDER_TO).append(productType).append(ConstantData.STRING_DIVIDER_TO).append(factoryName)

                .append(ConstantData.STRING_DIVIDER_TO).append(qty).toString();
    }
}
