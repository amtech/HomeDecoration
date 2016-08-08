package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.OrderTableModel;
import com.giants.hd.desktop.presenter.OrderReportPresenter;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.view.OrderReportViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Calendar;

/**订单报表界面实现层
 * Created by davidleen29 on 2016/8/8.
 */
public class Panel_Order_Report  extends BasePanel implements OrderReportViewer{

    private final OrderReportPresenter orderReportPresenter;

    OrderTableModel orderTableModel;
    private JPanel root;
    private Panel_Page pagePanel;
    private JHdTable orderTable;
    private JButton btn_search;
    private JTextField keys;
    private JDatePickerImpl date_start;
    private JDatePickerImpl date_end;

    public Panel_Order_Report(final OrderReportPresenter orderReportPresenter) {

        this.orderReportPresenter = orderReportPresenter;

        orderTableModel=new OrderTableModel();
        orderTable.setModel(orderTableModel);
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String startDate=date_start.getJFormattedTextField().getText().toString();
                String endDate=date_end.getJFormattedTextField().getText().toString();

                String key=keys.getText().toString();


                orderReportPresenter.search(key,startDate,endDate,0,pagePanel.getPageSize());


            }
        });

        date_start.getJFormattedTextField().setText("2015-01-01");
        try {
            date_end.getJFormattedTextField().setText(new HdDateComponentFormatter().valueToString(Calendar.getInstance()));
        } catch (ParseException e) {
            date_end.getJFormattedTextField().setText("2017-01-01");
        }


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                if (orderReportPresenter != null) {
                    String startDate = date_start.getJFormattedTextField().getText().toString();
                    String endDate = date_end.getJFormattedTextField().getText().toString();

                    String key = keys.getText().toString();
                    orderReportPresenter.search(key,startDate,endDate,  pageIndex, pageSize);
                }
            }
        });

        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2)
                {

                  int[] row=  JTableUtils.getSelectedRowSOnModel(orderTable);
                    if(row!=null&&row.length>=1)
                    {

                        ErpOrder order=orderTableModel.getItem(row[0]);
                        orderReportPresenter.loadOrderDetail(order);
                    }



                }
            }
        });

    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void setData(RemoteData<ErpOrder> erpOrderRemoteData) {
        if(erpOrderRemoteData.isSuccess())
        orderTableModel.setDatas(erpOrderRemoteData.datas);
        else
            showMesssage(erpOrderRemoteData.message);
        pagePanel.bindRemoteData(erpOrderRemoteData);
    }

    private void createUIComponents() {
        JDatePanelImpl picker = new JDatePanelImpl(null);
        date_start = new JDatePickerImpl(picker, new HdDateComponentFormatter());

          picker = new JDatePanelImpl(null);
        date_end = new JDatePickerImpl(picker, new HdDateComponentFormatter());
    }
}
