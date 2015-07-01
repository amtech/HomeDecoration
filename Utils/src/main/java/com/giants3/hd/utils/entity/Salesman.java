package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * 业务员表
 * Created by davidleen29 on 2015/7/1.
 */
@Entity(name = "T_Salesman")
public class Salesman {
    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;



    public String name;

    @Override
    public String toString() {
        return name;
    }
}
