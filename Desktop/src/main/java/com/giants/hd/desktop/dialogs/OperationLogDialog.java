package com.giants.hd.desktop.dialogs;

import com.giants3.hd.domain.api.ApiManager;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.OperationLogModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OperationLog;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

public class OperationLogDialog extends BaseSimpleDialog<OperationLog> {
    private JPanel contentPane;
    private JHdTable jt;


    private Class  aClass;
    private long id;

    @Inject
    ApiManager apiManager;

    @Inject
    OperationLogModel model;

    /**历史记录查看对话框
     * @param window
     * @param aClass
     * @param id
     */
    public OperationLogDialog(Window window,Class aClass, long id) {
        super(window);

        setTitle("历史操作记录查看");
        setContentPane(contentPane);
        this.aClass=aClass;
        this.id=id;
    }

    @Override
    protected RemoteData<OperationLog> readData() throws HdException {



      RemoteData<OperationLog> datas=  apiManager.readOperationLog(aClass.getSimpleName(),id);



        return datas;

    }

    @Override
    protected BaseTableModel<OperationLog> getTableModel() {
        return model;
    }

    @Override
    protected void init() {


        jt.setModel(model);

    }



}
