package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 *  产品详情界面
 */
public class Panel_ProductDetail  extends BasePanel{


    @Inject
    ApiManager apiManager;

    private JPanel panel1;
    private JTextField tf_product;
    private JLabel lable2;
    private JLabel title;
    private JButton bn_save;
    private JTextField textField1;
    private JLabel photo;


    private Product product;


    public Panel_ProductDetail(Product product)
    {

        super();

        this.product=product;

        updateView();


        addListener();

    }


    /*
    *   添加监听
     */
    private void addListener()
    {


        /**
         *
         */
        bn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                saveData(product);


            }
        });


        tf_product.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();



            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {

                product.setName(tf_product.getText().trim());


            }
        });
    }


    /**
     * 更新界面方法
     */
    private void updateView()
    {


        tf_product.setText(product.getName());

        if(product.photo!=null)
            photo.setIcon(new ImageIcon(product.photo));






    }

    public JPanel getPanel()
    {
        return panel1;
    }


    private void  saveData(final Product product)
    {


        new  SwingWorker< RemoteData<Product>,String >(){


            @Override
            protected RemoteData<Product> doInBackground() throws HdException {


                ProductDetail detail=new ProductDetail();
                detail.product=product;
                return   apiManager.saveProduct(detail);


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
