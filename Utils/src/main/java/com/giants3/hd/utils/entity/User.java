package com.giants3.hd.utils.entity;

/**
* 用户列表
*/
import javax.persistence.*;
import java.io.Serializable;

@Entity(name="T_User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Basic
    public String code;

    @Basic
    public String name;







}
