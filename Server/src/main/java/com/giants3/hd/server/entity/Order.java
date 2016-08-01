package com.giants3.hd.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单表  额外附加数据存在本系统的
 */
@Entity(name = "T_Order")
public class Order implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    //订单单号
    @Basic
    public String osNo;

    /**
     * 正唛
     */
    @Basic
    public String zhengmai;
    /**
     * 侧唛
     */
    @Basic
    public String cemai;
    /**
     * 内盒麦
     */
    @Basic
    public String neheimai;
    /**
     * 备注
     */
    @Basic
    public String memo;
    /**
     * 附件列表
     */
    @Basic
    @Lob
    public String attaches;

}
