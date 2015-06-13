package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 包装材质大分类
 *
 */


@Entity(name="T_PackMaterialClass")
public class PackMaterialClass   implements Serializable {


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



    public static final String  CLASS_BOX="外箱";

    public static final String CLASS_INSIDE_BOX="内盒";


    public static final String CLASS_JIAODAI="胶带";

    public static final String CLASS_ZHANSHIHE="展示盒";

    public static final String CLASS_QIPAODAI="气泡袋";

}
