package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.model.ProductMaterialTableModel;
import com.giants.hd.desktop.model.ProductPaintTableModel;
import com.giants.hd.desktop.widget.APanel;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.ProductPack;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JTextField tf_unit;
    private JComboBox cb_class;
    private JTextField tf_ingredient;
    private JTextField tf_cost;
    private JTextArea ta_spec;
    private JTextArea ta_memo;
    private JPanel cellPanel;
    private JTable productMaterialTable;
    private JTable productPaintTable;
    private JTextField tf_fob_2;
    private JTextField tf_fob_1;
    private JTextField tf_price_1;
    private JTextField tf_cost_1;
    private JTextField tf_price_2;
    private JTextField tf_cost_2;
    private JLabel lb_pack_1;
    private JLabel lb_pack_2;
    private JTextField tf_weight;


    private ProductDetail productDetail;

    @Inject
    ProductMaterialTableModel productMaterialTableModel;
    @Inject
    ProductPaintTableModel productPaintModel;


    public Panel_ProductDetail(Product product)
    {

        super();

        initComponent();

        if(product!=null)
             loadProductDetail(product);




    }


    /*
    *   添加监听
     */
    private void addListener()
    {


        /**
         * 保存功能
         */
        bn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                saveData(productDetail);


            }
        });

        photo.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()>=2)
                {

                    if(!StringUtils.isEmpty(productDetail.product.name)) {
                        ImageViewDialog.showDialog(getWindow(getPanel()),productDetail.product.name);
                    }else
                    {


                        JOptionPane.showMessageDialog(photo,"请输入货号...");
                    }

                }
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
     * 更新界面方法  负责数据的绑定
     */
    private void bindData()
    {


        Product product=productDetail.product;




        bindProductBaseInfo(product);








        productMaterialTableModel.setDatas(productDetail.materials);

        productPaintModel.setDatas(productDetail.paints);






    }


    /**\
     * 绑定产品的基本信息   基本信息+包装成本
     * @param product
     */
    private void bindProductBaseInfo(Product product)
    {



            tf_product.setText(product==null?"":product.getName());


                photo.setIcon(product.photo==null?null:new ImageIcon(product.photo));

                photo.setText(product==null?"产品图片":"");


            ta_spec.setText(product==null?"":product.getSpec());
            ta_memo.setText(product==null?"":product.getMemo());
            tf_date.setText(product==null?"":product.getrDate());
            tf_unit.setText(product==null?"":product.pTypeName);
              tf_unit.setText(product==null?"":product.pTypeName);
             tf_weight.setText(product==null?"":String.valueOf(product.getWeight()));
        //人工成本
        tf_cost.setText(product==null?"":String.valueOf(product.getCost1()));




        ProductPack pack1=null;
        ProductPack pack2=null;

        if(product!=null&&!ArrayUtils.isEmpty(product.packs ))
        {
              pack1=product.packs.get(0);
              pack2=product.packs.size()>1?product.packs.get(1):null;

        }else


        //设置包装信息
        lb_pack_1.setText(pack1==null?"普通包装":pack1.packName);
        lb_pack_2.setText(pack2==null?"加强包装":pack2.packName);


        tf_fob_1.setText(pack1==null?"":String.valueOf(pack1.fob));
        tf_cost_1.setText(pack1==null?"":String.valueOf(pack1.cost));
        tf_price_1.setText(pack1==null?"":String.valueOf(pack1.price));


        tf_fob_2.setText(pack2==null?"":String.valueOf(pack2.fob));
        tf_cost_2.setText(pack2==null?"":String.valueOf(pack2.cost));
        tf_price_2.setText(pack2==null?"":String.valueOf(pack2.price));


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
        bindData();
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




        //设置表格弹出菜单
        productMaterialTable.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    if (evt.isPopupTrigger()) {

                        JPopupMenu     popupMenu = new JPopupMenu();

                        JMenuItem insertItem = new JMenuItem("添加行");
                        JMenuItem deleteItem = new JMenuItem("删除行");



                        popupMenu.add(insertItem);

                        popupMenu.add(deleteItem);


                        insertItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                         int rowIndex=      productMaterialTable.getSelectedRow();

                                    productMaterialTableModel.addNewRow(rowIndex);

                            }
                        });

                        deleteItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int rowIndex=      productMaterialTable.getSelectedRow();

                                    productMaterialTableModel.deleteRow(rowIndex);

                            }
                        });


                        //  取得右键点击所在行
                        int row = evt.getY() / productMaterialTable.getRowHeight();
                        popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

                    }
                }
            }
        });

    }
}
