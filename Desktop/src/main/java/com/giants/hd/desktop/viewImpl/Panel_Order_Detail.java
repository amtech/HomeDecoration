package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.frames.OrderDetailFrame;
import com.giants.hd.desktop.model.OrderItemTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by david on 2016/3/30.
 */
public class Panel_Order_Detail extends BasePanel {
    private JPanel panel1;
    private JHdTable orderItemList;
    private JButton printPaint;
    private JButton showDetail;


    private OrderItemTableModel orderItemTableModel;

    private OrderDetailFrame detailFrame;

    public Panel_Order_Detail(OrderDetailFrame frame) {

        this.detailFrame = frame;
        orderItemTableModel = new OrderItemTableModel();
        orderItemList.setModel(orderItemTableModel);


        printPaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (orderItemList.getSelectedRow() <= -1) {
                    JOptionPane.showMessageDialog(Panel_Order_Detail.this.getWindow(), "请选一项订单产品");
                    return;
                }


                int modelRow = orderItemList.convertRowIndexToModel(orderItemList.getSelectedRow());
                ErpOrderItem orderItem = orderItemTableModel.getItem(modelRow);

                detailFrame.printPaint(orderItem);

            }
        });


        showDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (orderItemList.getSelectedRow() <= -1) {
                    JOptionPane.showMessageDialog(Panel_Order_Detail.this.getWindow(), "请选一项订单产品");
                    return;
                }


                int modelRow = orderItemList.convertRowIndexToModel(orderItemList.getSelectedRow());
                ErpOrderItem orderItem = orderItemTableModel.getItem(modelRow);
                detailFrame.showProductDetail(orderItem);


            }
        });


        orderItemList.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = orderItemList.getSelectedRow();
                    ErpOrderItem orderItem = orderItemTableModel.getItem(row);


                    int column = orderItemList.convertColumnIndexToModel(orderItemList.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 1) {
                        ImageViewDialog.showProductDialog(getWindow(), orderItem.prd_name, "", orderItem.url);
                    }


                }

            }
        });
    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    public void setErpOrder(ErpOrder erpOrder) {


    }


    public void setOrderItemList(List<ErpOrderItem> orderItemList) {
        orderItemTableModel.setDatas(orderItemList);
    }


}
