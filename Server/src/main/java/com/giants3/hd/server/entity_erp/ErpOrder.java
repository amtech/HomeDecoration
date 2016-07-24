package com.giants3.hd.server.entity_erp;

import com.giants3.hd.utils.StringUtils;

/**
 *
 * erp 订单信息
 *
 * table ：MF_POS
 * Created by david on 2016/2/28.
 */

public class ErpOrder {

    /**
     * 合同日期
     */
    public String os_dd;

    /**
     * 合同编号
     */
    public String os_no;
    /**
     * 客户名称
     */
    public String cus_no;

    /**
     * 客户订单号
     */
    public String cus_os_no;

    /**
     * 业务员
     */
    public String sal_no;


    /**
     * 业务中文名
     */
    public String sal_name;
    /**
     * 预计交期
     */
    public String est_dd;

    /**
     * 生产交期
     */
    public String so_data;

    /**
     * 备注
     */
    public String rem;



    /**
     * sql 数据读取后处理。
     */
    public void updateAfterSql() {

        //时间相关 取前10
        if(!StringUtils.isEmpty(os_dd)) {
            os_dd=os_dd.trim();
            if (os_dd.length() > 10) {
                os_dd=os_dd.substring(0, 10);
            }
        }

        if(!StringUtils.isEmpty(est_dd)) {
            est_dd=est_dd.trim();
            if (est_dd.length() > 10) {
                est_dd=est_dd.substring(0, 10);
            }
        }

        if(!StringUtils.isEmpty(so_data)) {
            so_data=so_data.trim();
            if (so_data.length() > 10) {
                so_data=so_data.substring(0, 10);
            }
        }

    }


}
