package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.OrderItemWorkFlowStateModel;
import com.giants.hd.desktop.presenter.OrderItemWorkFlowStatePresenter;
import com.giants.hd.desktop.view.OrderItemWorkFlowStateViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;

import javax.swing.*;
import java.util.List;

/**
 * Created by davidleen29 on 2017/1/20.
 */
public class Panel_OrderItemWorkFlowState extends BasePanel implements OrderItemWorkFlowStateViewer {
    private JPanel root;
    private JHdTable jt;
    private OrderItemWorkFlowStatePresenter presenter;


    OrderItemWorkFlowStateModel tableModel;

    public Panel_OrderItemWorkFlowState(OrderItemWorkFlowStatePresenter presenter) {

        this.presenter = presenter;
        tableModel = new OrderItemWorkFlowStateModel();
        jt.setModel(tableModel);
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
    public void setData(List<OrderItemWorkFlowState> datas) {


        tableModel.setDatas(datas);


    }
}
