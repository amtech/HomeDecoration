package com.giants3.hd.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单表细项目  额外附加数据存在本系统的
 */
@Entity(name = "T_OrderItem")
public class OrderItem implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    //订单单号
    @Basic
    public String osNo;

    //订单序号
    @Basic
    public int itm;


    /**
     * 验货日期
     */
    public String verifyDate;

    /**
     *  出柜日期
     */
    public String sendDate;

    /**
     * 包装信息
     */
    @Basic @Lob
    public String packageInfo;


    /**
     * 唛头
     */
    public String  maitou;


    /**
     * 挂钩说明
     */
    public String   guagou ;

}
