package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2017/1/18.
 */

@Entity(name = "T_ErpOrderItemWorkFlowState")
public class ErpOrderItemWorkFlowState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String os_no;
    public String prd_no;

    public String currentFlowCode;
    public String currentFlowName;

    public String nextFlowCode;
    public String nextFlowName;


    public String gjh;

    public  int orderQty;

    public String scsx;

    /**
     * 已经发送数量
     */
    public int sentQty;

    public long createTime;
    public String createTimeString;


    public long senderId;
    public String senderName;

    public long sendTime;
    public String sendTimeString;


    public long receverId;
    public String receverName;

    public long receiveTime;
    public String receiveTimeString;

    public String getMessage() {

        return "";

    }
}
