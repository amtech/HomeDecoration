package com.giants3.hd.utils.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
* 产品信息类
*/

@Entity(name="PRDT1")
public class Prdt1  implements Serializable {

    @Basic
    private String wh;




    @Id
    private String prd_mark;

    @Basic
    private float qty;



    @Id
    private String prd_no;





    public String getWh() {
        return wh;
    }

    public String getPrd_mark() {
        return prd_mark;
    }

    public float getQty() {
        return qty;
    }



    public String getPrd_no() {
        return prd_no;
    }

    public void setWh(String wh) {
        this.wh = wh;
    }

    public void setPrd_mark(String prd_mark) {
        this.prd_mark = prd_mark;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }



    public void setPrd_no(String prd_no) {
        this.prd_no = prd_no;
    }




}
