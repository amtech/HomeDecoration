package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.OrderDetailDialog;
import com.giants.hd.desktop.viewImpl.Panel_Order_List;
import com.giants.hd.desktop.viewImpl.Panel_ProductList;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by david on 2015/11/23.
 */
public class OrderListInternalFrame extends BaseInternalFrame implements OrderListAdapter{
    Panel_Order_List panelOrderList;
    public OrderListInternalFrame( ) {
        super("订单列表");
    }

    @Override
    protected Container getCustomContentPane() {

        panelOrderList=new   Panel_Order_List(this);
        return panelOrderList.getRoot();
    }

    @Override
    public void search(String key, int pageIndex, int pageSize) {





        UseCaseFactory.getInstance().createOrderListUseCase(key,pageIndex,pageSize).execute(new Subscriber<RemoteData<ErpOrder>>() {
            @Override
            public void onCompleted() {
                panelOrderList.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panelOrderList.hideLoadingDialog();
                panelOrderList.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpOrder> erpOrderRemoteData) {

                panelOrderList.setData(erpOrderRemoteData);
            }
        });

        panelOrderList.showLoadingDialog();


    }

    /**
     * 订单详情读取
     * @param erpOrder
     */
    @Override
    public void loadOrderDetail(ErpOrder erpOrder) {


        Dialog dialog=new OrderDetailDialog(SwingUtilities.getWindowAncestor(this),erpOrder);
        dialog.setVisible(true);






    }


}
