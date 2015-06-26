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
    public int knd;
    public String spec;
    public String rem;
    @Transient
    public float price;


}
