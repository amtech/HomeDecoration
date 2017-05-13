package com.giants3.hd.utils.entity;

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
    public String workFlowName;
    public String osNo;
    public String prdNo;
    /**
     * 完成百分比。
     */
    public float percentage;

}
