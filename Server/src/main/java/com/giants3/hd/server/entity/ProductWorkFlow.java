package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**产品生产流程数据  每个产品的生产流程  是整个生产的子集/
 *
 * 不同产品的 不同部分生产流程也不一样   铁 木  pu
 * Created by davidleen29 on 2016/9/4.
 */
@Entity(name = "T_ProductWorkFlow")
public class ProductWorkFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public  long productId;
    public String productName;




    long  productTypeId;
    String productTypeName;

    long workFlowId;
    String workFlowStep;
    String workFlowName;



}
