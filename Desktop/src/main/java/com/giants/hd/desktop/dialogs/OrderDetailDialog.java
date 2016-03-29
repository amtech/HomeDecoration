package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.viewImpl.Panel_Order_Detail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import rx.Subscriber;

import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * Created by davidleen29 on 2015/8/24.
 */
public class OrderDetailDialog extends BaseDialog<ErpOrder> {
    @Inject
    ApiManager apiManager;
    private Panel_Order_Detail panelOrderDetail;
    public OrderDetailDialog(Window window,ErpOrder order) {
        super(window);
        setTitle("订单详情");
        panelOrderDetail=new Panel_Order_Detail();
        setContentPane(panelOrderDetail.getRoot());
        loadOrderItems(order);
    }


    public void loadOrderItems(ErpOrder order)
    {


        UseCaseFactory.getInstance().createOrderItemListUseCase(order.os_no).execute(new Subscriber<java.util.List<ErpOrderItem>>() {
            @Override
            public void onCompleted() {
                panelOrderDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panelOrderDetail.hideLoadingDialog();
                panelOrderDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<ErpOrderItem> orderItems) {

                panelOrderDetail.setOrderItemList(orderItems);
            }
        });

        panelOrderDetail.showLoadingDialog();
    }


}
