package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.StockOutItemTableModel;
import com.giants.hd.desktop.presenter.StockOutDetailPresenter;
import com.giants.hd.desktop.view.StockOutDetailViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;

import javax.swing.*;

/**
 * 出库界面详情界面
 * Created by davidleen29 on 2016/7/14.
 */
public class Panel_StockOutDetail extends BasePanel implements StockOutDetailViewer {
    private JButton btn_addPicture;
    private JPanel root;
    private JHdTable tb;
    private JTextField tf_ck_no;
    private JTextField tf_cus;
    private JTextField tf_dd;
    private JTextField tf_mdg;
    private JTextField tf_tdh;
    private JTextField tf_gsgx;
    private StockOutDetailPresenter presenter;

    public Panel_StockOutDetail(StockOutDetailPresenter presenter) {
        this.presenter = presenter;

        init();
    }

    private void init() {

        tableModel = new StockOutItemTableModel();

        tb.setModel(tableModel);
    }

    StockOutItemTableModel tableModel;

    @Override
    public JComponent getRoot() {
        return root;
    }


    @Override
    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail) {


        tf_ck_no.setText(erpStockOutDetail.erpStockOut.ck_no);

        tf_dd.setText(erpStockOutDetail.erpStockOut.ck_dd);
        tf_cus.setText(erpStockOutDetail.erpStockOut.cus_no);
        tf_gsgx.setText(erpStockOutDetail.erpStockOut.gsgx);
        tf_mdg.setText(erpStockOutDetail.erpStockOut.mdg);
        tf_tdh.setText(erpStockOutDetail.erpStockOut.tdh);
        tableModel.setDatas(erpStockOutDetail.items);

    }
}
