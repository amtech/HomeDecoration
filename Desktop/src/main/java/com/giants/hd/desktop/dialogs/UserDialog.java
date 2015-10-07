package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants.hd.desktop.model.BaseTableModel;

import com.giants.hd.desktop.model.UserModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDialog extends BaseSimpleDialog<User> {
    private JPanel contentPane;
    private JButton btn_save;
    private JHdTable jt;



    @Inject
    ApiManager apiManager;

    @Inject
    UserModel model;
    public UserDialog(Window window) {
        super(window);


    }

    @Override
    protected RemoteData<User> saveData(java.util.List<User> datas) throws HdException {
        return apiManager.saveUsers(datas);
    }

    @Override
    protected RemoteData<User> readData() throws HdException {
        return apiManager.readUsers();
    }

    @Override
    protected BaseTableModel<User> getTableModel() {
        return model;
    }

    @Override
    protected void init() {
        setTitle("用户列表");
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
