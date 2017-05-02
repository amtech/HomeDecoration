package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.ErpOrder;
import com.giants3.hd.utils.entity.ErpOrderItem;

/**
 * Created by davidleen29 on 2017/4/19.
 */
public interface OrderItemForArrangeListPresenter extends ListPresenter<ErpOrderItem> {
    void search(String key, int pageIndex, int pageSize);
}
