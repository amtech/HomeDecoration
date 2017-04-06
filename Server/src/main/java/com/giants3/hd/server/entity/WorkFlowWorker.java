package com.giants3.hd.server.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * 流程节点工作人员定位
 *
 *    Created by davidleen29 on 2015/7/1.
 *
 */
@Entity(name = "T_WorkFlowWorker")
public class WorkFlowWorker implements Serializable {

    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 流程
     */
    public long workFlowId;    /**
     * 流程
     */
    public long workFlowStep;

    /**
     *流程id
     */
    public String workFlowName;
    /**
     * 用户id
     */
    public long userId;
    /**
     * 用户name
     */
    public String  userName;
    /**
     * 发送 1 ，接收 2
     */
    public  int operateType;

    /**
     * 排产类型   铁001 木  010 pu 100  all 111
     */
    public  int  arrangeType;






}
