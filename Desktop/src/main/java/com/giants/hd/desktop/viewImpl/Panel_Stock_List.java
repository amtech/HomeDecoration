package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.StockOutTableModel;
import com.giants.hd.desktop.presenter.StockListPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.StockOut;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 界面   出库列表
 * Created by davidleen29 on 2016/7/12.
 */
public class Panel_Stock_List extends BasePanel {
    private JPanel root;
    private JPanel panel1;
    private JHdTable tb;
    private Panel_Page pageController;
    private JTextField jtf_key;
    private JButton btn_search;
    @Inject
    StockOutTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }

    StockListPresenter presenter;


    public Panel_Stock_List(final StockListPresenter presenter) {
        this.presenter = presenter;

        tb.setModel(tableModel);

        pageController.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(), pageIndex, pageSize);
            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (presenter != null)
                    presenter.search(jtf_key.getText().trim(), 0, pageController.getPageSize());

            }
        });



        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {



                if(presenter!=null&&e.getClickCount()==2)
                {
                    int row = tb.getSelectedRow();
                    int modelRow = tb.convertRowIndexToModel(row);
                    ErpStockOut erpOrder = tableModel.getItem(modelRow);
                    presenter.onListItemClick(erpOrder);
                }



            }
        });

    }


    public void setData(RemoteData<ErpStockOut> remoteData) {


        if (remoteData != null && remoteData.isSuccess()) {
            tableModel.setDatas(remoteData.datas);
            pageController.bindRemoteData(remoteData);

        }


    }
}
