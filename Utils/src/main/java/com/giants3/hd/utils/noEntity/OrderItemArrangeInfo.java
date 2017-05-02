package com.giants3.hd.utils.noEntity;

/**订单款项的排厂信息
 * Created by davidleen29 on 2017/4/19.
 */
public class OrderItemArrangeInfo {



    public  long orderItemId;
    public String orderName;
    public String productName;
    public String pVersion;
    public long qty;
    public boolean hasWorkFlowSet;


    public boolean produceType;

    /**
     * 是否可以排厂
     */
    public boolean canArrange;
}
