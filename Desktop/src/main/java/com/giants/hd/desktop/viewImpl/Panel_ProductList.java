package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * 产品列表界面
 */
@Singleton
public class Panel_ProductList extends BasePanel {

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


    public Panel_ProductList() {
        super();


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(productName.getText().toString().trim());


            }
        });

        productName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(productName.getText().toString().trim());

            }
        });


        productTable.setModel(tableModel);


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
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), product.getName(), product.getpVersion(),product.url);
                    } else {

                        HdSwingUtils.showDetailPanel(product, getRoot());

                    }


                }

            }
        });


        //添加按钮事件
        bn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                HdSwingUtils.showDetailPanel(null, getRoot());


            }
        });


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {
                searchProduct(productName.getText().trim(), pageIndex, pageSize);
            }
        });


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchProduct("");
            }
        });


        bn_add.setVisible(AuthorityUtil.getInstance().addProduct());


    }


    private void searchProduct(String productNameValue) {

        searchProduct(productNameValue, 0, pagePanel.getPageSize());

    }

    private void searchProduct(final String productNameValue, final int pageIndex, final int pageSize) {


        new HdSwingWorker<Product, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {

                return apiManager.readProductList(productNameValue, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<Product> data) {


                pagePanel.bindRemoteData(data);
                tableModel.setDatas(data.datas);
                productTable.scrollRectToVisible(productTable.getCellRect(0 - 1, 0, true));



            }
        }.go();


    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }
}
