package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**产品生产流程数据  每个产品的生产流程  是整个生产的子集/
 * Created by davidleen29 on 2016/9/4.
 */
@Entity(name = "T_ProductWorkFlow")
public class ProductWorkFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public  long productId;
    public String productName;


    public long workFlowId;
    public String workFlowName;


    /**
     * 当前序号。
     */
    public int productFlowIndex;


}
