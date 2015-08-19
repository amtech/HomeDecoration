package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.QuoteAuthModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class QuoteAuthDialog extends BaseSimpleDialog<QuoteAuth> {
    private JPanel contentPane;
    private JButton save;
    private JHdTable jt;



    QuoteAuthModel quoteAuthModel;

    @Inject
    ApiManager apiManager;
    public QuoteAuthDialog(Window window) {
        super(window);
        setTitle( "报价细分权限");
        setContentPane(contentPane);









    }

    @Override
    protected RemoteData<QuoteAuth> readData() throws HdException {
        return apiManager.readQuoteAuth() ;
    }

    @Override
    protected BaseTableModel<QuoteAuth> getTableModel() {
        return quoteAuthModel;
    }

    @Override
    protected void init() {



        quoteAuthModel=new QuoteAuthModel();
        jt.setModel(quoteAuthModel);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                doSaveWork();
            }
        });

    }



    @Override
    protected   RemoteData<QuoteAuth> saveData(List<QuoteAuth> datas)throws HdException
    {

        return apiManager.saveQuoteAuthList(datas);
    }

}
