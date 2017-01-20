package com.giants3.hd.utils.entity;

/**
 * 外厂家数据
 * Created by davidleen29 on 2017/1/14.
 */
public class OutFactory {


    public long  id;
    public String name;
    public String manager;
    public String telephone;
    public String address;



    @Override
    public String toString() {
        return name+"("+manager+")";
    }
}
