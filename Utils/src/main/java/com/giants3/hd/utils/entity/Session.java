package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 登录状态记录表
 */
@Entity(name="T_Session")
public class Session implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    @ManyToOne
    public User user;

    @Basic
    public  String token;
    @Basic
    public long loginTime;
    @Basic
    public String loginIp;


}
