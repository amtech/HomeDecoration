package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.BufferData;
import com.giants.hd.desktop.model.ProductMaterialTableModel;
import com.giants.hd.desktop.model.ProductPaintTableModel;
import com.giants.hd.desktop.widget.APanel;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.RemoteData;
import com.google.inject.Inject;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 *  产品详情界面
 */
public class Panel_ProductDetail  extends BasePanel{


    @Inject
    ApiManager apiManager;




    private JTextField tf_product;
    private JLabel lable2;
    private JLabel title;
    private JButton bn_save;
    private JTextField tf_date;
    private JLabel photo;
    private JTabbedPane tabbedPane1;
    private JTextField tf_unit;
    private JComboBox<PClass> cb_class;
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
    private JTextField tf_version;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JScrollPane contentPane;


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



        //分类挑选
        cb_class.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if(e.getStateChange()==ItemEvent.SELECTED)
                {
                    PClass pClass= (PClass) e.getItem();
                    productDetail.product.pClassId=pClass.id;
                    productDetail.product.pClassName=pClass.name;
                }



            }
        });



        JTextField jtf=new JTextField();
        jtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object= e.getDocument();
                Logger.getLogger("TAG").info("insertUpdate"+object.toString());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object= e.getDocument();
                Logger.getLogger("TAG").info("removeUpdate"+object.toString());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                Document object= e.getDocument();

                Logger.getLogger("TAG").info("changedUpdate"+object.toString());

            }
        });


        //回车键触发
        jtf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                String text=((JTextField)e.getSource()).getText().trim();


                SearchMaterialDialog dialog=new SearchMaterialDialog(getWindow(contentPane),text);


                dialog.pack();
                dialog.setLocationRelativeTo(productMaterialTable);
                dialog.setVisible(true);
                Material material=dialog.getResult();

                 int rowIndex=        productMaterialTable.convertRowIndexToModel( productMaterialTable.getSelectedRow());

                productMaterialTableModel.setMaterial(material,rowIndex);





            }
        });

        DefaultCellEditor editor=new DefaultCellEditor(jtf);


        //
        editor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {

                Object object= e.getSource();

            }

            @Override
            public void editingCanceled(ChangeEvent e) {


                Object object= e.getSource();


            }
        });


        //productMaterialTable.setCellEditor(editor);
        productMaterialTable.setDefaultEditor(Object.class,editor);


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



            //设置产品分类
            int selectClassIndex=-1;
            if(product!=null)
            for(int i=0,count=cb_class.getItemCount();i<count;i++)
            {

                if(cb_class.getItemAt(i).id==product.pClassId)
                {

                    selectClassIndex=i;
                    break;
                }

            }
        cb_class.setSelectedIndex(selectClassIndex);




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







    public JComponent getPanel()
    {
        return contentPane ;
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

                        JOptionPane.showMessageDialog(contentPane,"数据保存成功!");

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
       cb_class=new JComboBox<PClass>();
        for(PClass pClass:BufferData.pClasses)
        {cb_class.addItem(pClass);
        }


    }





    /**
     * 初始化控件装配。
     */
    public void initComponent()
    {

       productMaterialTable.setModel(productMaterialTableModel);
        productPaintTable.setModel(productPaintModel);
        productMaterialTable.setRowHeight(30);
        productPaintTable.setRowHeight(30);




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
