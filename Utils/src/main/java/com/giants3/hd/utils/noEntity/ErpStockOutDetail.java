package com.giants3.hd.utils.noEntity;

import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 出库详情列表
 * Created by davidleen29 on 2016/7/18.
 */
public class ErpStockOutDetail   implements Serializable {

    public ErpStockOut erpStockOut;
    public List<ErpStockOutItem> items;

}
