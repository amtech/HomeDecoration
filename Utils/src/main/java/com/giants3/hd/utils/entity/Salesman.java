package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *
 * 业务员表
 * Created by davidleen29 on 2015/7/1.
 */
@Entity(name = "T_Salesman")
public class Salesman implements Serializable,Valuable {
    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;



    public String name;
    public String code;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(code)&&StringUtils.isEmpty(name
        );
    }
}
