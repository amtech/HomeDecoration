package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统模块表
 */
@Entity(name="T_Module")
public class Module implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;



    @Basic
    public String name;

    @Basic
    public String title;

    @Override
    public String toString() {

        return "["+name+"] "+title;
    }
}
