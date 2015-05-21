package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.model.ProductMaterialTableModel;
import com.giants.hd.desktop.model.ProductPaintTableModel;
import com.giants.hd.desktop.widget.APanel;
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
    private JTextField tf_date;
    private JLabel photo;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JComboBox cb_class;
    private JTextField tf_ingredient;
    private JTextField tf_cost;
    private JTextArea ta_spec;
    private JTextArea ta_memo;
    private JPanel cellPanel;
    private JTable productMaterialTable;
    private JTable productPaintTable;


    private ProductDetail productDetail;

    @Inject
    ProductMaterialTableModel productMaterialTableModel;
    @Inject
    ProductPaintTableModel productPaintModel;


    public Panel_ProductDetail(Product product)
    {

        super();

        initComponent();

        loadProductDetail(product);




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


                saveData(productDetail);


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

                productDetail.product.setName(tf_product.getText().trim());


            }
        });
    }


    /**
     * 更新界面方法
     */
    private void updateView()
    {


        Product product=productDetail.product;

        tf_product.setText(product.getName());

        if(product.photo!=null)
            photo.setIcon(new ImageIcon(product.photo));






    }

    public JPanel getPanel()
    {
        return panel1;
    }


    private void  saveData(final ProductDetail product)
    {


        new  SwingWorker< RemoteData<Product>,String >(){


            @Override
            protected RemoteData<Product> doInBackground() throws HdException {




                return   apiManager.saveProduct(product);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<Product> productRemoteData=get();

                    if(productRemoteData.isSuccess())
                    {


                        //TODO 显示保存成功

                        JOptionPane.showMessageDialog(panel1,"数据保存成功!");

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    /**
     * 加载产品详情信息
     */
    private void loadProductDetail(final Product product)
    {



        new  SwingWorker< RemoteData<ProductDetail>,Long >(){


            @Override
            protected RemoteData<ProductDetail> doInBackground() throws HdException {



                return   apiManager.loadProductDetail(product.id);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<ProductDetail> productRemoteData=get();


                    ProductDetail detail=productRemoteData.datas.get(0);


                    initPanel(detail);







                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();


    }

    /**
     * 数据初始化 产品详界面
     * @param detail
     */
    private void initPanel(ProductDetail detail) {

        this.productDetail=detail;
        updateView();
        addListener();

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        cellPanel=new APanel();
        //cellPanel.setGridColor(Color.GRAY);
       // cellPanel.setPaintInBackground(false);
    }





    /**
     * 初始化控件装配。
     */
    public void initComponent()
    {

       productMaterialTable.setModel(productMaterialTableModel);
        productPaintTable.setModel(productPaintModel);

    }
}