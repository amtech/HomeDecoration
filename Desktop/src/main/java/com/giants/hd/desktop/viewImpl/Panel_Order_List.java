package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.OrderListAdapter;
import com.giants.hd.desktop.frames.OrderListInternalFrame;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.OrderTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity_erp.ErpOrder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**订单面板  presenter
 * Created by david on 2016/3/29.
 */
public class Panel_Order_List extends BasePanel{
    private JPanel panel1;
    private JButton btn_search;
    private JTextField keys;
    private JHdTable orderTable;
    private Panel_Page pagePanel;

    OrderTableModel orderTableModel;

    private OrderListAdapter adapter;
    public Panel_Order_List(OrderListAdapter mAdapter)
    {
        this.adapter=mAdapter;
        orderTableModel=new OrderTableModel();
        orderTable.setModel(orderTableModel);

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {
                adapter.search(keys.getText().trim(), pageIndex, pageSize);
            }
        });

        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                adapter.search(keys.getText().trim(),0,pagePanel.getPageSize());
            }
        });



        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2)
                {
                    int row = orderTable.getSelectedRow();
                    int modelRow=orderTable.convertRowIndexToModel(row);
                    ErpOrder erpOrder = orderTableModel.getItem(modelRow);
                    adapter.loadOrderDetail(erpOrder);

                }
            }
        });

    }

    public void setData(RemoteData<ErpOrder> remoteData)
    {

        pagePanel.bindRemoteData(remoteData);
        orderTableModel.setDatas(remoteData.datas);
    }

    @Override
    public JComponent getRoot() {
          return panel1;
    }
}
