package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *  流程跳转确认信
 *
 * Created by davidleen29 on 2016/9/3.
 */
@Entity(name = "T_WorkFlowMessage")
public class WorkFlowMessage
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    public int fromFlowIndex;
    public String  fromFlowName;


    /**
     * 接受流程id
     */
    public int toFlowIndex;
    public  String  toFlowName;


    /**
     * 订单项id
     */
    public long orderItemId;

    /**
     * 订单数量
     */
    public int orderItemQty;



    /**
     * 订单id
     */
    public  long orderId;
    public String orderName;

    /**
     * 产品信息
     */
    public long productId;
    public String productName;
    /**
     * 单位
     */
    public String unit;



    /**
     * 移交数量
     */
    public int transportQty;






    /**
     * 当前状态  0 未处理  1 已处理
     */
    public int  state;




    public String createTimeString;
    public long createTime;

    public   long receiveTime;
    public String receiveTimeString;


    public long checkTime;
    public String checkTimeString;


            ;

}
