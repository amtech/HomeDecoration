package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.StockInAndSubmitTableModel;
import com.giants.hd.desktop.presenter.StockXiaokuDetailPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.StockSubmit;
import com.giants3.hd.utils.entity.StockXiaoku;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * 销库单详情
 * Created by davidleen29 on 2016/12/12.
 */
public class Panel_StockXiaokuDetail  extends BasePanel{
    private final StockXiaokuDetailPresenter presenter;
    private JPanel rootpanel;
    private JHdTable tb;
    private JButton export;
    private JLabel guihao;
    private JLabel fapiao;
    private JLabel guixing;
    private JLabel fengqian;
    private JLabel tcgs;

    StockInAndSubmitTableModel tableModel;

    public Panel_StockXiaokuDetail(final StockXiaokuDetailPresenter presenter) {
        this.presenter = presenter;
        tableModel=new StockInAndSubmitTableModel();
        tb.setModel(tableModel);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.exportExcel();
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
        return rootpanel;
    }


    public void setItemData(RemoteData<StockSubmit> remoteData)
    {

        tableModel.setDatas(remoteData.datas);
    }

    public void bindData(StockXiaoku xiaoku)
    {

        tcgs.setText(xiaoku.tcgs);
        fengqian.setText(xiaoku.xhfq);
        guihao.setText(xiaoku.xhgh);
        guixing.setText(xiaoku.xhgx);
        fapiao.setText(xiaoku.xhph);
    }
}
