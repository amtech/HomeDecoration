package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.SalesmanModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Salesman;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SalesmanDialog extends BaseSimpleDialog<Salesman> {
    private JPanel contentPane;
    private JButton btn_save;
    private JHdTable jt;


    @Inject
    ApiManager apiManager;

    @Inject
    SalesmanModel model;


    public SalesmanDialog(Window window) {
        super(window);
    }

    @Override
    protected RemoteData<Salesman> saveData(List<Salesman> datas) throws HdException {
        return apiManager.saveSalesmans(datas);
    }

    @Override
    protected RemoteData<Salesman> readData() throws HdException {
        return apiManager.readSalesmans();
    }

    @Override
    protected BaseTableModel<Salesman> getTableModel() {
        return model;
    }

    @Override
    protected void init() {
        setTitle("业务员列表");
        setContentPane(contentPane);
        jt.setModel(model);

        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSaveWork();
            }
        });
    }
}
