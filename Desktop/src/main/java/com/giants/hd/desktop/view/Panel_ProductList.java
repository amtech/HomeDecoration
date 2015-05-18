package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.exceptions.HdException;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 产品列表界面
 */
@Singleton
public class Panel_ProductList {

    @Inject
    ApiManager apiManager;


    private JPanel panel1;
    private JButton btn_search;
    private JCheckBox sortAsc;
    private JLabel product_title;
    private JTextField productName;
    private JTable productTable;
    private JLabel message;
    private JButton first;
    private JButton previous;
    private JButton next;
    private JButton last;
    private JTextField textField2;
    private JButton turnTo;
    private JComboBox pageRows;


    public JPanel getRootPanel() {
        return panel1;
    }


    public Panel_ProductList() {
        super();



        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct();


            }
        });



    }


    private  void searchProduct()
    {

        final String productNameValue=productName.getText().toString().trim();



        new  SwingWorker< RemoteData<Product>,String >(){


            @Override
            protected RemoteData<Product> doInBackground() throws HdException {

             return   apiManager.readProductList(productNameValue,1,100);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<Product> productRemoteData=get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }
}
