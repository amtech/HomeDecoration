package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.CustomerModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerDialog extends BaseSimpleDialog<Customer>
{
    private JPanel contentPane;
    private JButton btn_save;
    private JHdTable tb;



    @Inject
    CustomerModel model;
    @Inject
    ApiManager apiManager;

    public CustomerDialog(Window window) {
        super(window);

    }


    @Override
    public void init() {
        setContentPane(contentPane);
        setTitle("客户列表");
        tb.setModel(model);
        setMinimumSize(new Dimension(600, 400));
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                doSaveWork();
            }
        });

    }



    @Override
    protected RemoteData<Customer> readData() throws HdException {
        return apiManager.readCustomers();
    }

    @Override
    protected BaseTableModel<Customer> getTableModel() {
        return model;
    }


    @Override
    protected RemoteData<Customer> saveData(List<Customer> datas) throws HdException {
        return apiManager.saveCustomers(datas);
    }
}
