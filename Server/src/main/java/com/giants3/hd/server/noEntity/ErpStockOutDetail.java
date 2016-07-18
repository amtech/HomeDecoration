package com.giants3.hd.server.noEntity;

import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;

import java.util.List;

/**
 *
 * 出库详情列表
 * Created by davidleen29 on 2016/7/18.
 */
public class ErpStockOutDetail {

    public ErpStockOut erpStockOut;
    public List<ErpStockOutItem> items;
}
