package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.model.AppQuotationItemTableModel;
import com.giants.hd.desktop.mvp.presenter.AppQuotationDetailPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationDetailViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * 广交会报价详情
 * Created by david on 2016/3/30.
 */
public class Panel_AppQuotation_Detail extends BasePanel implements AppQuotationDetailViewer {
    private JPanel panel1;
    private JHdTable table;

    private JTextField tf_qNumber;
    private JTextField tf_qdate;
    private JTextField tf_salesman;
    private JTextField tf_customer;

    private JTextField tf_vDate;
    private JTextArea ta_memo;


    private AppQuotationItemTableModel tableModel;

    private AppQuotationDetailPresenter orderDetailPresenter;

    public Panel_AppQuotation_Detail(AppQuotationDetailPresenter presenter) {

        this.orderDetailPresenter = presenter;
        tableModel = new AppQuotationItemTableModel();
        table.setModel(tableModel);


        table.addMouseListener(new MouseInputAdapter() {


            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            private void showMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {


                    return;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = table.getSelectedRow();
                    QuotationItem item = tableModel.getItem(row);


                    int column = table.convertColumnIndexToModel(table.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 1) {
                        ImageViewDialog.showDialog(getWindow(), HttpUrl.loadPicture(item.photoUrl),item.productName );
                    }


                }

            }
        });


    }

    @Override
    public void bindDetail(QuotationDetail detailData) {


        bindData(detailData.quotation);

        tableModel.setDatas(detailData.items);


    }

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Override
    public void bindData(Quotation item) {
        tf_qNumber.setText(item.qNumber);
        tf_qdate.setText(item.qDate);
        tf_customer.setText(item.customerCode+item.customerName);

        tf_salesman.setText(item.salesman );
        tf_vDate.setText(item.vDate);
        ta_memo.setText(item.memo);
    }


}
