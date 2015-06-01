package com.giants3.hd.utils.entity;

import javax.persistence.*;

/**
 * 包装材质类型
 *
 */


@Entity(name="T_PackMaterialType")
public class PackMaterialType {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;




    @Basic
    public String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
