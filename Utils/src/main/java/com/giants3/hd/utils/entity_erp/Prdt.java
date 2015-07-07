package com.giants3.hd.utils.entity_erp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 *  erp 系统货品数据
 */
//@Entity(name="PRDT")
public class Prdt implements Serializable{



//    @Id

    public String prd_no;
    public String name;
    public String ut;
    public String knd;
    public String spec;
    public String rem;

    public float price;


    public float wLong;
    public float wWidth;
    public float wHeight;
    public float available=1;
    public float discount=0;
    public int type;


    public String classId;
    public String className;
}
