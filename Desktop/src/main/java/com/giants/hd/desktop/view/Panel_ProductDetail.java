package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.BufferData;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.module.ProductDetailTableModule;
import com.giants.hd.desktop.widget.APanel;
import com.giants.hd.desktop.widget.TablePopMenu;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 产品详情界面
 */
public class Panel_ProductDetail extends BasePanel {


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
    private JTextField tf_constitute;
    private JTextField tf_cost;
    private JTextArea ta_spec;
    private JTextArea ta_memo;
    private JPanel cellPanel;
    private JTable tb_conceptus_cost;

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
    private JTabbedPane tabbedPane2;
    private JTable tb_pack_cost;
    private JTable tb_pack_wage;
    private JTable tb_product_paint;
    private JTextField jtf_paint_wage;
    private JTextField jtf_paint_cost;
    private JTabbedPane tabbedPane3;
    private JTable tb_conceptus_wage;
    private JTabbedPane tabbedPane4;
    private JTable tb_assemble_cost;
    private JTable tb_assemble_wage;
    private JTextField jtf_conceptus_wage;
    private JTextField jtf_conceptus_cost;
    private JTextField jtf_assemble_wage;
    private JTextField jtf_assemble_cost;


    private ProductDetail productDetail;




    @Inject
    ProductMaterialTableModel conceptusMaterialTableModel;

    @Inject
    ProductMaterialTableModel assembleMaterialTableModel;


    @Inject
    ProductWageTableModel conceptusWageTableModel;
    @Inject
    ProductWageTableModel assembleWageTableModel;


    @Inject
    ProductPaintTableModel productPaintModel;


    ProductDetailTableModule module;


    /**
     * 油漆表格监听对象
     */
    TableModelListener productPaintModelListener;


    public Panel_ProductDetail(Product product) {

        super();

        module=new ProductDetailTableModule(this);


        initComponent();

        if (product != null)
            loadProductDetail(product);
        else {
            productDetail = new ProductDetail();
            productDetail.product = new Product();
        }


        addListener();
    }


    /*
    *   添加监听
     */
    private void addListener() {


        /**
         * 保存功能
         */
        bn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {

                    collectionData();
                    saveData(productDetail);
                } catch (HdUIException exception) {
                    JOptionPane.showMessageDialog(exception.component, exception.message);
                    exception.component.requestFocus();
                }


            }
        });


        photo.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {


                    if (!StringUtils.isEmpty(tf_product.getText().trim())) {
                        ImageViewDialog.showDialog(getWindow(getPanel()), tf_product.getText().trim(), tf_version.getText().trim());
                    } else {


                        JOptionPane.showMessageDialog(photo, "请输入货号...");
                    }

                }
            }
        });


//        //产品名称修改
//        tf_product.getDocument().addDocumentListener(new DocumentListener() {
//            public void changedUpdate(DocumentEvent e) {
//                warn();
//            }
//
//            public void removeUpdate(DocumentEvent e) {
//                warn();
//            }
//
//            public void insertUpdate(DocumentEvent e) {
//                warn();
//            }
//
//            public void warn() {
//
//                productDetail.product.setName(tf_product.getText().trim());
//
//
//            }
//        });


//        //分类挑选
//        cb_class.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    PClass pClass = (PClass) e.getItem();
//                    productDetail.product.pClassId = pClass.id;
//                    productDetail.product.pClassName = pClass.name;
//                }
//            }
//        });



        //定义表格模型数据改变监听对象
        productPaintModelListener=new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //数据改变  更新统计数据
                //汇总计算油漆工资与材料费用
                float paintWage=0;
                float paintCost=0;
                for(ProductPaint paint:productPaintModel.getDatas())
                {
                    paintWage+=paint.processPrice;
                    paintCost+=paint.materialCost+paint.ingredientCost;

                }
                productDetail.product.updatePaintData(paintCost, paintWage);



                //汇总计算白胚材料
                float conceptusCost=0;
                for(ProductMaterial material:conceptusMaterialTableModel.getDatas())
                {
                    conceptusCost+=material.getAmount();
                }
                productDetail.product.conceptusCost=conceptusCost;
                //汇总计算组白胚工资
                float conceptusWage=0;
                for(ProductWage wage:conceptusWageTableModel.getDatas())
                {
                    conceptusWage+=wage.getAmount();
                }
                productDetail.product.conceptusWage=conceptusWage;



                //汇总计算组装材料
                float assembleCost=0;
                for(ProductMaterial material:assembleMaterialTableModel.getDatas())
                {
                    assembleCost+=material.getAmount();
                }
                productDetail.product.assembleCost=assembleCost;

                //汇总计算组装工资
                float assembleWage=0;
                for(ProductWage wage:assembleWageTableModel.getDatas())
                {
                    assembleWage+=wage.getAmount();
                }
                productDetail.product.assembleWage=assembleWage;





                /**
                 * 重新计算总成本
                 */
                productDetail.product.calculateTotalCost();


                bindStatisticsValue(productDetail.product);

            }
        };

        //注册表模型的监听
        productPaintModel.addTableModelListener(productPaintModelListener);
        assembleMaterialTableModel.addTableModelListener(productPaintModelListener);
        assembleWageTableModel.addTableModelListener(productPaintModelListener);
        conceptusMaterialTableModel.addTableModelListener(productPaintModelListener);
        conceptusWageTableModel.addTableModelListener(productPaintModelListener);



        //配置表格的自定义编辑输入
        configTableCellEditor(tb_conceptus_cost);
        configTableCellEditor(tb_assemble_cost);
        configTableCellEditor(tb_product_paint);



    }


    /**
     * 收集数据   将各当前数据 存入数据中
     *
     * @see
     */
    private void collectionData() throws HdUIException {


        Product product = productDetail.product;

        //货号
        String productName = tf_product.getText().trim();
        //检验输入


        if (StringUtils.isEmpty(productName)) {
            throw HdUIException.create(tf_product, "请输入货号。。。");
        }

        product.setName(productName);

        //规格
        String ta_spcValue = ta_spec.getText().trim();
        product.setSpec(ta_spcValue);


        //备注
        String ta_memoValue = ta_memo.getText().trim();
        product.setMemo(ta_memoValue);

        //产品分类
        PClass pClassData = (PClass) cb_class.getSelectedItem();
        if (pClassData == null) {
            throw HdUIException.create(cb_class, "请选择产品类别。。。");
        }
        product.setpClassId(pClassData.id);
        product.setpClassName(pClassData.name);

        //产品版本号
        String tf_versionValue = tf_version.getText().trim();
        product.setpVersion(tf_versionValue);

        //产品的材料构成  文字描述
        String tf_constituteValue = tf_constitute.getText().trim();
        product.setConstitute(tf_constituteValue);

        //产品单位

        String tf_unitValue = tf_unit.getText().trim();
        product.setpPUnitName(tf_unitValue);


        //产品净重

        try {
            float tf_weightValue = Float.valueOf(tf_weight.getText().trim());
            product.setWeight(tf_weightValue);
        } catch (Throwable t) {
            t.printStackTrace();

            throw HdUIException.create(tf_weight, "输入产品净重，数值型数据");
        }


        //日期
        String tf_dateValue = tf_date.getText().trim();
        //日期格式验证
        product.setrDate(tf_dateValue);


        //产品油漆数据

        List<ProductPaint> paints = productPaintModel.getDatas();
        //数据检验
        int size = paints.size();
        for (int i = 0; i < size; i++) {

            ProductPaint paint = paints.get(i);


            int rowIndex = tb_product_paint.convertRowIndexToView(i);


            if (StringUtils.isEmpty(paint.processName))
                throw HdUIException.create(tb_product_paint, "第" + (rowIndex + 1) + "行数据有误，工序名称为必须");

        }


        productDetail.paints = paints;



        List<ProductMaterial> conceptusMaterial= conceptusMaterialTableModel.getDatas();
        //TODO  白胚材料的 数据检验


        productDetail.conceptusMaterials=conceptusMaterial;




        List<ProductMaterial> assembleMaterials=assembleMaterialTableModel.getDatas();
        //TODO  组装材料的 数据检验


        productDetail.assembleMaterials=assembleMaterials;



        List<ProductWage> assembleWages=assembleWageTableModel.getDatas();
        //TODO  组装工资的 数据检验


        productDetail.assembleWages=assembleWages;


        List<ProductWage> conceptusWages=conceptusWageTableModel.getDatas();
        //TODO  白胚工资的 数据检验


        productDetail.conceptusWages=conceptusWages;






    }


    /**
     * 更新界面方法  负责数据的绑定
     */
    private void bindData() {


        Product product = productDetail.product;

        bindProductBaseInfo(product);



        bindStatisticsValue(product);






        bindTableDatas(assembleMaterialTableModel, productDetail.assembleMaterials);
        bindTableDatas(assembleWageTableModel,productDetail.assembleWages);

        bindTableDatas(conceptusMaterialTableModel, productDetail.conceptusMaterials);
        bindTableDatas(conceptusWageTableModel,productDetail.conceptusWages);

        bindTableDatas(productPaintModel,productDetail.paints);
    }


    /**
     * 重新表格绑定数据
     * @param model
     * @param datas
     * @param <T>
     */
    private <T> void  bindTableDatas(BaseTableModel<T> model, List<T> datas)
    {
        //为了避免触发监听，先移除后添加
        model.removeTableModelListener(productPaintModelListener);
        model.setDatas(datas);
        model.addTableModelListener(productPaintModelListener);

    }






    /**
     * \
     * 绑定产品的基本信息   基本信息+包装成本
     *
     * @param product
     */
    private void bindProductBaseInfo(Product product) {


        tf_product.setText(product == null ? "" : product.getName());


        photo.setIcon(product.photo == null ? null : new ImageIcon(product.photo));

        photo.setText(product == null ? "产品图片" : "");


        ta_spec.setText(product == null ? "" : product.getSpec());
        ta_memo.setText(product == null ? "" : product.getMemo());
        tf_date.setText(product == null ? "" : product.getrDate());
        tf_unit.setText(product == null ? "" : product.pUnitName);
        tf_weight.setText(product == null ? "" : String.valueOf(product.getWeight()));

        tf_constitute.setText(product == null ? "" : product.getConstitute());
        tf_version.setText(product == null ? "" : product.getpVersion());
        //人工成本
        tf_cost.setText(product == null ? "" : String.valueOf(product.getCost1()));


        //设置产品分类
        int selectClassIndex = -1;
        if (product != null)
            for (int i = 0, count = cb_class.getItemCount(); i < count; i++) {

                if (cb_class.getItemAt(i).id == product.pClassId) {

                    selectClassIndex = i;
                    break;
                }

            }
        cb_class.setSelectedIndex(selectClassIndex);


        //绑定包装汇总信息
        ProductPack pack1 = null;
        ProductPack pack2 = null;

        if (product != null && !ArrayUtils.isEmpty(product.packs)) {
            pack1 = product.packs.get(0);
            pack2 = product.packs.size() > 1 ? product.packs.get(1) : null;

        }


        //设置包装信息
        lb_pack_1.setText(pack1 == null ? "普通包装" : pack1.packName);
        lb_pack_2.setText(pack2 == null ? "加强包装" : pack2.packName);


        tf_fob_1.setText(pack1 == null ? "" : String.valueOf(pack1.fob));
        tf_cost_1.setText(pack1 == null ? "" : String.valueOf(pack1.cost));
        tf_price_1.setText(pack1 == null ? "" : String.valueOf(pack1.price));


        tf_fob_2.setText(pack2 == null ? "" : String.valueOf(pack2.fob));
        tf_cost_2.setText(pack2 == null ? "" : String.valueOf(pack2.cost));
        tf_price_2.setText(pack2 == null ? "" : String.valueOf(pack2.price));

    }


    public JComponent getPanel() {
        return contentPane;
    }

    /**
     * 执行数据保存
     *
     * @param product
     */

    private void saveData(final ProductDetail product) {


        new SwingWorker<RemoteData<Product>, String>() {


            @Override
            protected RemoteData<Product> doInBackground() throws HdException {


                return apiManager.saveProduct(product);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<Product> productRemoteData = get();

                    if (productRemoteData.isSuccess()) {


                        //TODO 显示保存成功

                        JOptionPane.showMessageDialog(contentPane, "数据保存成功!");

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
    private void loadProductDetail(final Product product) {


        new SwingWorker<RemoteData<ProductDetail>, Long>() {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws HdException {


                return apiManager.loadProductDetail(product.id);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<ProductDetail> productRemoteData = get();


                    ProductDetail detail = productRemoteData.datas.get(0);


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
     *
     * @param detail
     */
    private void initPanel(ProductDetail detail) {

        this.productDetail = detail;
        bindData();
        //addListener();

    }


    private void createUIComponents() {

        cellPanel = new APanel();
        //cellPanel.setGridColor(Color.GRAY);
        // cellPanel.setPaintInBackground(false);
        cb_class = new JComboBox<PClass>();
        for (PClass pClass : BufferData.pClasses) {
            cb_class.addItem(pClass);
        }


    }


    /**
     * 初始化控件装配。
     */
    public void initComponent() {


        module.initComponent();

        tb_conceptus_cost.setModel(conceptusMaterialTableModel);

        tb_assemble_cost.setModel(assembleMaterialTableModel);

        tb_product_paint.setModel(productPaintModel);



        tb_conceptus_wage.setModel(conceptusWageTableModel);

        tb_assemble_wage.setModel(assembleWageTableModel);





        tb_conceptus_cost.setRowHeight(30);
        tb_assemble_cost.setRowHeight(30);
        tb_pack_cost.setRowHeight(30);
        tb_conceptus_wage.setRowHeight(30);
        tb_assemble_wage.setRowHeight(30);


        //监听器， 监听表格右键点击功能
        MouseAdapter adapter = new MouseAdapter() {


            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            private void showMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JTable source = (JTable) e.getSource();
                    JPopupMenu menu = new TablePopMenu(source);
                    //  取得右键点击所在行
                    int row = e.getY() / tb_conceptus_cost.getRowHeight();
                    menu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        };

        //设置表格点击监听
        tb_conceptus_cost.addMouseListener(adapter);
        tb_conceptus_wage.addMouseListener(adapter);

        tb_assemble_cost.addMouseListener(adapter);
        tb_assemble_wage.addMouseListener(adapter);
        tb_product_paint.addMouseListener(adapter);
        tb_pack_cost.addMouseListener(adapter);





    }


    /**
     * 绑定汇总数据
     */
    private void bindStatisticsValue(Product product)
    {
        jtf_paint_cost.setText(String.valueOf(product.paintCost));
        jtf_paint_wage.setText(String.valueOf(product.paintWage));

        jtf_assemble_cost.setText(String.valueOf(product.assembleCost));
        jtf_assemble_wage.setText(String.valueOf(product.assembleWage));

        jtf_conceptus_cost.setText(String.valueOf(product.conceptusCost));
        jtf_conceptus_wage.setText(String.valueOf(product.conceptusWage));

        tf_cost.setText(String.valueOf(product.productCost));



    }

    /**
     *  配置表格的 弹出选择框
     */
    private void configTableCellEditor(final JTable table)
    {


        //定制表格的编辑功能 弹出物料选择单

        JTextField jtf = new JTextField();
        jtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
//                Logger.getLogger("TAG").info("insertUpdate" + object.toString());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
//                Logger.getLogger("TAG").info("removeUpdate" + object.toString());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                Document object = e.getDocument();

//                Logger.getLogger("TAG").info("changedUpdate" + object.toString());

            }
        });


        //回车键触发
        jtf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = ((JTextField) e.getSource()).getText().trim();
                SearchMaterialDialog dialog = new SearchMaterialDialog(getWindow(contentPane), text);
                dialog.pack();
                dialog.setLocationRelativeTo(table);
                dialog.setVisible(true);
                Material material = dialog.getResult();
                if (material != null) {

                    int rowIndex = table.convertRowIndexToModel(table.getSelectedRow());

                  if(  table.getModel() instanceof Materialable)
                  {

                      ((Materialable)table.getModel()).setMaterial(material,rowIndex);
                  }

                }
            }
        });

        DefaultCellEditor editor = new DefaultCellEditor(jtf);


//        //
//        editor.addCellEditorListener(new CellEditorListener() {
//            @Override
//            public void editingStopped(ChangeEvent e) {
//
//                Object object = e.getSource();
//
//            }
//
//            @Override
//            public void editingCanceled(ChangeEvent e) {
//
//
//                Object object = e.getSource();
//
//
//            }
//        });



        table.setDefaultEditor(Material.class, editor);

    }
}
