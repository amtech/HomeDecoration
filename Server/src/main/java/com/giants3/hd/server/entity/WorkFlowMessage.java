package com.giants3.hd.server.entity;

/**
 *  流程跳转确认信
 *
 * Created by davidleen29 on 2016/9/3.
 */
public class WorkFlowMessage
{

    public long id;

    /**
     * 发起流程id
     */
    public long fromFlowId;

    public String  fromFlowName;


    /**
     * 接受流程id
     */
    public long toFlowId;
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
     * 是否自动审核，  否 需要审核
     */
    public boolean  autoCheck;

    /**
     * 发送者
     */
    public  String sender;
    public long  senderId;
    /**
     * 接收者
     */
    public String  receiver;
    public long  receiverId;


    /**
     * 审核者
     */
    public String checker;
    public long  checkerId;


    /**
     * 当前状态  1 已发出  2 已接受  3 已审核
     */
    public int  state;




}
