package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 外厂家数据
 * Created by davidleen29 on 2017/1/14.
 */

@Entity(name="T_OutFactory")
public class OutFactory {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long  id;


    public String name;
    public String manager;
    public String telephone;
    public String address;


}
