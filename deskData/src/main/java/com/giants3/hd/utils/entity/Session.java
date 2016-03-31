package com.giants3.hd.utils.entity;

import java.io.Serializable;

/**
 * 登录状态记录表
 */

public class Session implements Serializable{


    public long id;



    public User user;


    public  String token;

    public long loginTime;

    public String loginIp;


}
