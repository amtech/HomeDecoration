package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.JTableUtils;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.interf.PageListener;
import com.giants3.hd.utils.exception.HdException;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

/**
 * 产品列表界面
 */
@Singleton
public class Panel_ProductList  extends BasePanel {

    @Inject
    ApiManager apiManager;


    private JPanel panel1;
    private JButton btn_search;
    private JCheckBox sortAsc;
    private JLabel product_title;
    private JTextField productName;
    private JTable productTable;
    private JButton bn_add;
    private Panel_Page pagePanel;


    @Inject
    ProductTableModel tableModel;

    public JPanel getRootPanel() {
        return panel1;
    }


    public Panel_ProductList() {
        super();




        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(productName.getText().toString().trim());


            }
        });





        productTable.setModel(tableModel);
        productTable.setRowHeight(100);


        JTableUtils.setJTableColumnsWidth(productTable, 800, 40, 60, 60, 60, 60, 60);


        productTable.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = productTable.getSelectedRow();
                    Product product = tableModel.getItem(row);


                    int column = productTable.convertColumnIndexToModel(productTable.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showDialog(getWindow(getRootPanel()),product.getName(),product.getpVersion());
                    } else {

                        JDialog dialog = new JDialog(getWindow(getRootPanel()));
                        dialog.setModal(true);
                        Panel_ProductDetail panel_productDetail = new Panel_ProductDetail(product);


                        dialog.setContentPane(panel_productDetail.getPanel());
                        dialog.pack();
                        dialog.setLocationRelativeTo(getRootPanel());
                        dialog.setVisible(true);

                    }


                }

            }
        });


        //添加按钮事件
        bn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JDialog dialog = new JDialog(getWindow(getRootPanel()));
                dialog.setModal(true);
                Panel_ProductDetail panel_productDetail = new Panel_ProductDetail(null);
                dialog.setContentPane(panel_productDetail.getPanel());

                dialog.pack();
                dialog.setVisible(true);



            }
        });




        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {
                searchProduct(productName.getText().toString().trim(),pageIndex,pageSize);
            }
        });

    }


    private  void searchProduct(String productNameValue)
    {





        searchProduct(productNameValue,0,pagePanel.getPageSize());

    }

    private  void searchProduct(final String productNameValue,final int pageIndex,final int pageSize)
    {




       final  LoadingDialog dialog = new LoadingDialog(getWindow(getRootPanel()), new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {

           }
       });



        new  SwingWorker< RemoteData<Product>,String >(){


            @Override
            protected RemoteData<Product> doInBackground() throws HdException {

             return   apiManager.readProductList(productNameValue,pageIndex,pageSize);


            }

            @Override
            protected void done() {
                super.done();
                dialog.setVisible(false);
                dialog.dispose();
                try {
                    RemoteData<Product> productRemoteData=get();

                    pagePanel.bindRemoteData(productRemoteData);


                    tableModel.setDatas(productRemoteData.datas);





                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
        dialog.setVisible(true);

    }


}
