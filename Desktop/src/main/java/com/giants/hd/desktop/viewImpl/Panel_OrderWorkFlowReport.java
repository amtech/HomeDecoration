package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.OrderItemWorkFlowTableModel;
import com.giants.hd.desktop.presenter.OrderWorkFlowReportPresenter;
import com.giants.hd.desktop.view.OrderWorkFlowReportViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.ErpOrderItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by davidleen29 on 2016/9/25.
 */
public class Panel_OrderWorkFlowReport extends BasePanel implements OrderWorkFlowReportViewer {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JHdTable jt_undone;
    private JHdTable jt_work;
    private JTextField tf_key;
    private JButton search;
    private Panel_Page pagePanel;
    private OrderWorkFlowReportPresenter presenter;

    OrderItemWorkFlowTableModel unCompleteModel;
    OrderItemWorkFlowTableModel workFlowTableModel;
    public Panel_OrderWorkFlowReport(final OrderWorkFlowReportPresenter presenter) {
        this.presenter = presenter;

        unCompleteModel = new OrderItemWorkFlowTableModel();
        unCompleteModel.setRowAdjustable(false);

        jt_undone.setModel(unCompleteModel);

        workFlowTableModel=new OrderItemWorkFlowTableModel();
        jt_work.setModel(workFlowTableModel);
        workFlowTableModel.setRowAdjustable(false);


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (presenter != null) {
                    String key = tf_key.getText().trim();
                    presenter.search(key, pageIndex, pageSize);
                }
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String key = tf_key.getText().trim();
                presenter.search(key, 0, pagePanel.getPageSize());
            }
        });
        tf_key.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String key = tf_key.getText().trim();
                presenter.search(key, 0, pagePanel.getPageSize());
            }
        });
    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void setData(RemoteData<ErpOrderItem> remoteData) {

        if (remoteData.isSuccess()) {
            workFlowTableModel.setDatas(remoteData.datas);
            pagePanel.bindRemoteData(remoteData);
        }else
        {
            showMesssage(remoteData.message);
        }
    }


    @Override
    public void setUnCompleteData(RemoteData<ErpOrderItem> remoteData) {



        if (remoteData.isSuccess()) {
            unCompleteModel.setDatas(remoteData.datas);
        }else
        {
            showMesssage(remoteData.message);
        }
    }
}
