package com.giants3.hd.utils.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Administrator on 2014/9/23.
 */
@Entity(name="PRDT")
public class Prdt implements Serializable{

    @Id
    private String prd_no;

    @Basic
    private String ut;

    public String getPrd_no() {
        return prd_no;
    }

    public void setPrd_no(String prd_no) {
        this.prd_no = prd_no;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }



}
