package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.OrderListAdapter;
import com.giants.hd.desktop.model.OrderItemTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;

import javax.swing.*;
import java.util.List;

/**
 * Created by david on 2016/3/30.
 */
public class Panel_Order_Detail extends  BasePanel{
    private JPanel panel1;
    private JHdTable orderItemList;




    private OrderItemTableModel orderItemTableModel;

    public Panel_Order_Detail()
    {

        orderItemTableModel=new OrderItemTableModel();
        orderItemList.setModel(orderItemTableModel);
    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    public void setErpOrder(ErpOrder erpOrder)
    {

    }


    public void setOrderItemList(List<ErpOrderItem > orderItemList)
    {
        orderItemTableModel.setDatas(orderItemList);
    }


}
