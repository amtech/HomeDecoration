package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**流程报告数据
 * Created by davidleen29 on 2017/2/28.
 */
@Entity(name="T_ErpWorkFlowReport")
public class ErpWorkFlowReport {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String workFlowCode;
    public int workFlowStep;
    public String workFlowName;
    public String osNo;
    public String prdNo;
    public int itm;
    public String pVersion;
    /**
     * 完成百分比。
     */
    public float percentage;


    /**
     * 当前流程的类型数量  铁 木
     */
    public int typeCount;
}
