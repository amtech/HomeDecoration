package com.giants3.hd.utils.entity;

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


    public int fromFlowStep;
    public String  fromFlowName;


    /**
     * 接受流程id
     */
    public int toFlowStep;
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
     * 消息内容
     */
    public String name;




    /**
     * 当前状态  0 未处理  1 已经接受  2 审核通过 3  审核拒绝  4 返工
     */
    public int  state;




    public String createTimeString;
    public long createTime;

    public   long receiveTime;
    public String receiveTimeString;


    public long checkTime;
    public String checkTimeString;
    public String  thumbnail;
    public String url;




    public  String memo;


    /**
     * 订单排厂id
     */
    public long orderItemWorkFlowId;

    /**
     * 订单项目流程状态id
     */
    public long orderItemWorkFlowStateId;
    ;


    //冗余字段
    public String productTypeName;


    //冗余字段
    public String factoryName;


    /**
     * 返工
     */
    public static final int STATE_REWORK =4;

    /**
     * 审核不通过
     */
    public static final int STATE_REJECT=3;
    /**
     * 已接收
     */
    public static final int STATE_PASS=2;

    /**
     * 已接收
     */
    public static final int STATE_RECEIVE=1;

    /**
     * 发送
     */
    public static final int STATE_SEND=0;


    public static  final  String NAME_SUBMIT="提交";
    public static  final  String NAME_REWORK="返工";


}
