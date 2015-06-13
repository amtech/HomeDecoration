package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品工资信息
 */
@Entity(name="T_ProductWage")
public class ProductWage  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

    @Basic
    public long productId;


    @Basic
    public long flowId;

    @Basic
    public String flowName;




    @Basic
    public long processId;

    @Basic
    public String processCode;
    @Basic
    public String processName;

    @Basic
    public float  price;


    @Basic

   public  float amount;


    @Basic
    public String memo ;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getFlowId() {
        return flowId;
    }

    public void setFlowId(long flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public long getProcessId() {
        return processId;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
