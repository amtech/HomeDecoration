package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.dialogs.CopyProductDialog;
import com.giants.hd.desktop.dialogs.MaterialDetailDialog;
import com.giants.hd.desktop.local.*;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.widget.APanel;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants.hd.desktop.widget.header.ColumnGroup;
import com.giants.hd.desktop.widget.header.GroupableTableHeader;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    private JLabel photo;
    private JTabbedPane tabbedPane1;
    private JTextField tf_unit;
    private JComboBox<PClass> cb_class;
    private JTextField tf_constitute;
    private JTextField tf_unpack_cost;
    private JTextArea ta_spec;
    private JTextArea ta_memo;
    private JPanel cellPanel;
    private JTable tb_conceptus_cost;


    private JTextField tf_fob;
    private JTextField tf_price;
    private JTextField tf_cost;


    private JTextField tf_weight;
    private JTextField tf_version;
    private JTextField tf_gross_profit;

    private JTextField tf_inboxCount;
    private JTextField tf_long;
    private JScrollPane contentPane;
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

    private JDatePickerImpl date;
    private JCheckBox cb_xiankang;
    private Panel_XK panel_xiankang;
    private JButton btn_export;
    private JTextField jtf_cost1;
    private JTextField jtf_cost8;
    private JTextField jtf_cost6;
    private JTextField jtf_cost7;
    private JTextField jtf_cost5;
    private JTextField jtf_cost11_15;
    private JTextField tf_price_dollar;
    private JTextField tf_volume;
    private JTextField tf_width;
    private JTextField tf_height;
    private JTextField tf_quantity;
    private JTextField tf_cost4;
    private JComboBox cb_pack;
    private JTabbedPane tabbedPane5;
    private JButton btn_copy;
    private JTextField jtf_mirror_size;
    private JButton btn_delete;


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


    @Inject
    ProductPackMaterialTableModel packMaterialTableModel;
    @Inject
    ProductWageTableModel packWageTableModel;


    // ProductDetailTableModule module;


    /**
     * 油漆表格监听对象
     */
    TableModelListener allTableModelListener;


    /**
     * 表格菜单的回调接口
     */
    private TablePopMenu.TableMenuLister tableMenuLister;


   private  DocumentListener tf_quantityListener;



    public Panel_ProductDetail(ProductDetail productDetail) {
    super();

        initComponent();
        addListener();
        initPanel(productDetail);


    }

    public Panel_ProductDetail(Product product) {

        super();

        //module=new ProductDetailTableModule(this);


        initComponent();

        if (product != null)
            loadProductDetail(product);
        else {
            productDetail = new ProductDetail();
            productDetail.product = new Product();
            setProductInfoToPackageModel();
        }


        addListener();
    }


    /**
     * 将产品信息 关联到包装表模型
     */
    private void setProductInfoToPackageModel()
    {

        //设置包装材料的 产品引用数据
        packMaterialTableModel.setProduct(productDetail.product);
        //设置包装材料的 产品引用数据
        packWageTableModel.setProduct(productDetail.product);
    }

    /*
    *   添加监听
     */
    private void addListener() {





        //数量改变
         tf_quantity.getDocument().addDocumentListener(tf_quantityListener);


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


        btn_copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(productDetail.product.id<=0)
                {

                    JOptionPane.showMessageDialog((Component) e.getSource(), "产品数据未建立，请先保存");
                    return;

                }

                //显示对话框


                CopyProductDialog dialog=new CopyProductDialog(SwingUtilities.getWindowAncestor(getRoot()),productDetail.product);
                dialog.pack();
                dialog.setMinimumSize(new Dimension(400, 300));
                dialog.setLocationByPlatform(true);
                dialog.setModal(true);
                dialog.setVisible(true);

                if(dialog.getResult()!=null)
                HdSwingUtils.showDetailPanel(getRoot(),dialog.getResult());










            }
        });


        /**
         * 删除
         */
        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               if(listener!=null) listener.delete();




            }
        });


        photo.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {


                    if (!StringUtils.isEmpty(tf_product.getText().trim())) {
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), tf_product.getText().trim(), tf_version.getText().trim());
                    } else {


                        JOptionPane.showMessageDialog(photo, "请输入货号...");
                    }

                }
            }
        });


        cb_xiankang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                panel_xiankang.setVisible(cb_xiankang.isSelected());
            }
        });


        //定义表格模型数据改变监听对象
        allTableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //数据改变  更新统计数据
                //汇总计算油漆工资与材料费用

                productDetail.conceptusMaterials=conceptusMaterialTableModel.getDatas();
                productDetail.conceptusWages=conceptusWageTableModel.getDatas();

                productDetail.assembleMaterials=assembleMaterialTableModel.getDatas();
                productDetail.assembleWages=assembleWageTableModel.getDatas();
                productDetail.paints=productPaintModel.getDatas();
                productDetail.packMaterials=packMaterialTableModel.getDatas();
                productDetail.packWages=packWageTableModel.getDatas();


                productDetail.updateProductStatistics();
                bindStatisticsValue(productDetail.product);

            }
        };

        //注册表模型的监听
        productPaintModel.addTableModelListener(allTableModelListener);
        assembleMaterialTableModel.addTableModelListener(allTableModelListener);
        assembleWageTableModel.addTableModelListener(allTableModelListener);
        conceptusMaterialTableModel.addTableModelListener(allTableModelListener);
        conceptusWageTableModel.addTableModelListener(allTableModelListener);
        packMaterialTableModel.addTableModelListener(allTableModelListener);
        packWageTableModel.addTableModelListener(allTableModelListener);

        //配置表格的自定义编辑输入
        configTableCellEditor(tb_conceptus_cost);
        configTableCellEditor(tb_assemble_cost);
        configTableCellEditor(tb_product_paint);
        configTableCellEditor(tb_pack_cost);










    }



    public void configGroupHeader(JTable jTable)
    {
        ColumnGroup g_name = new ColumnGroup("产品规格");
        TableColumnModel columnModel = jTable.getColumnModel();

        g_name.add(columnModel.getColumn(3));
        g_name.add(columnModel.getColumn(4));
        g_name.add(columnModel.getColumn(5));
        ColumnGroup g_lang = new ColumnGroup("毛料规格");
        g_lang.add(columnModel.getColumn(6));
        g_lang.add(columnModel.getColumn(7));
        g_lang.add(columnModel.getColumn(8));

        GroupableTableHeader header = (GroupableTableHeader) jTable.getTableHeader();

        header.addColumnGroup(g_name);
        header.addColumnGroup(g_lang);
        //jTable.setTableHeader(header);

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


        //包装类型
        Pack pack= (Pack) cb_pack.getSelectedItem();
        if(pack==null)
        {
            throw HdUIException.create(cb_pack, "请选择产品包装类型。。。");
        }
        product.pack=pack;



        //产品版本号
        String tf_versionValue = tf_version.getText().trim();
        product.setpVersion(tf_versionValue);

        //产品的材料构成  文字描述
        String tf_constituteValue = tf_constitute.getText().trim();
        product.setConstitute(tf_constituteValue);

        //产品单位

        String tf_unitValue = tf_unit.getText().trim();
        product.setpPUnitName(tf_unitValue);


        //镜面尺寸

        String mirrorSizeValue=jtf_mirror_size.getText();
        product.mirrorSize=mirrorSizeValue;


        //是否咸康数据

        boolean isXiangkang = cb_xiankang.isSelected();
        if (isXiangkang) {
            Xiankang xiankang = new Xiankang();
            panel_xiankang.getData(xiankang);
            product.xiankang = xiankang;
        }


        //产品净重

        try {
            float tf_weightValue = Float.valueOf(tf_weight.getText().trim());
            product.setWeight(tf_weightValue);
        } catch (Throwable t) {
            t.printStackTrace();

            throw HdUIException.create(tf_weight, "输入产品净重，数值型数据");
        }


        date.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //日期
        String tf_dateValue = date.getJFormattedTextField().getText().toString();


        //日期格式验证
        product.setrDate(tf_dateValue);


        //产品油漆数据

        List<ProductPaint> paints = productPaintModel.getDatas();
//        //数据检验
//        int size = paints.size();
//        for (int i = 0; i < size; i++) {
//
//            ProductPaint paint = paints.get(i);
//
//
//            int rowIndex = tb_product_paint.convertRowIndexToView(i);
//
//
//            if (StringUtils.isEmpty(paint.processName))
//                throw HdUIException.create(tb_product_paint, "第" + (rowIndex + 1) + "行数据有误，工序名称为必须");
//
//        }


        productDetail.paints = paints;


        List<ProductMaterial> conceptusMaterial = conceptusMaterialTableModel.getDatas();
        //TODO  白胚材料的 数据检验


        productDetail.conceptusMaterials = conceptusMaterial;


        List<ProductMaterial> assembleMaterials = assembleMaterialTableModel.getDatas();
        //TODO  组装材料的 数据检验


        productDetail.assembleMaterials = assembleMaterials;


        List<ProductWage> assembleWages = assembleWageTableModel.getDatas();
        //TODO  组装工资的 数据检验


        productDetail.assembleWages = assembleWages;


        List<ProductWage> conceptusWages = conceptusWageTableModel.getDatas();
        //TODO  白胚工资的 数据检验


        productDetail.conceptusWages = conceptusWages;


        /////////////包装

        List<ProductMaterial> packMaterials = packMaterialTableModel.getDatas();
        //TODO  包装材料 数据检验


        productDetail.packMaterials = packMaterials;


        List<ProductWage> packWages = packWageTableModel.getDatas();
        //TODO   包装工资  数据检验


        productDetail.packWages = packWages;


    }


    /**
     * 更新界面方法  负责数据的绑定
     */
    private void bindData() {


        Product product = productDetail.product;

        bindProductBaseInfo(product);


        bindStatisticsValue(product);


        bindTableDatas(assembleMaterialTableModel, productDetail.assembleMaterials);
        bindTableDatas(assembleWageTableModel, productDetail.assembleWages);

        bindTableDatas(conceptusMaterialTableModel, productDetail.conceptusMaterials);
        bindTableDatas(conceptusWageTableModel, productDetail.conceptusWages);

        bindTableDatas(productPaintModel, productDetail.paints);


        bindTableDatas(packMaterialTableModel, productDetail.packMaterials);
        bindTableDatas(packWageTableModel, productDetail.packWages);
    }


    /**
     * 重新表格绑定数据
     *
     * @param model
     * @param datas
     * @param <T>
     */
    private <T> void bindTableDatas(BaseTableModel<T> model, List<T> datas) {
        //为了避免触发监听，先移除后添加
        model.removeTableModelListener(allTableModelListener);
        model.setDatas(datas);
        model.addTableModelListener(allTableModelListener);

    }


    /**
     * \
     * 绑定产品的基本信息   基本信息+包装成本
     *
     * @param product
     */
    private void bindProductBaseInfo(Product product) {


        tf_product.setText(product == null ? "" : product.getName());


        jtf_mirror_size.setText(product == null ? "" : product.mirrorSize);



        photo.setIcon(product.photo == null ? null : new ImageIcon(product.photo));

        photo.setText(product == null ? "产品图片" : "");


        ta_spec.setText(product == null ? "" : product.getSpec());
        ta_memo.setText(product == null ? "" : product.getMemo());
        date.getJFormattedTextField().setText(product == null ? "" : product.getrDate());

        tf_unit.setText(product == null ? "" : product.pUnitName);
        tf_weight.setText(product == null ? "" : String.valueOf(product.getWeight()));

        tf_constitute.setText(product == null ? "" : product.getConstitute());
        tf_version.setText(product == null ? "" : product.getpVersion());
        //人工成本
        tf_unpack_cost.setText(product == null ? "" : String.valueOf(product.getCost1()));


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

        cb_pack.setSelectedItem(product.pack);


        boolean isXiangkang = product != null && product.xiankang != null;


        cb_xiankang.setSelected(isXiangkang);
        panel_xiankang.setVisible(isXiangkang);
        if (isXiangkang)
            panel_xiankang.setData(product.xiankang);






        tf_fob.setText(product == null ? "" : String.valueOf(product.fob));
        tf_cost.setText(product == null ? "" : String.valueOf(product.cost));
        tf_price.setText(product == null ? "" : String.valueOf(product.price));





    }


    /**
     * 执行数据保存
     *
     * @param product
     */

    private void saveData(final ProductDetail product) {


        new HdSwingWorker<Product, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {

                return apiManager.saveProduct(product);


            }

            @Override
            public void onResult(RemoteData<Product> data) {

                if (data.isSuccess()) {


                    //TODO 显示保存成功

                    JOptionPane.showMessageDialog(contentPane, "数据保存成功!");

                }
            }
        }.go();


    }


    /**
     * 加载产品详情信息
     */
    private void loadProductDetail(final Product product) {


        new HdSwingWorker<ProductDetail, Long>(getWindow(getRoot())) {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {
                return apiManager.loadProductDetail(product.id);
            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {

                ProductDetail detail = data.datas.get(0);


                initPanel(detail);
            }
        }.go();


    }

    /**
     * 数据初始化 产品详界面
     *
     * @param detail
     */
    private void initPanel(ProductDetail detail) {

        this.productDetail = detail;


        setProductInfoToPackageModel();
        bindData();
        //addListener();

    }


    private void createUIComponents() {

       // cellPanel = new APanel();
        //cellPanel.setGridColor(Color.GRAY);
        // cellPanel.setPaintInBackground(false);
        cb_class = new JComboBox<PClass>();
        for (PClass pClass : BufferData.pClasses) {
            cb_class.addItem(pClass);
        }

        cb_pack=  new JComboBox<Pack>();
        for (Pack pack : BufferData.packs) {
            cb_pack.addItem(pack);
        }



        JDatePanelImpl picker = new JDatePanelImpl(null);
        date = new JDatePickerImpl(picker, new HdDateComponentFormatter());



    }


    /**
     * 初始化控件装配。
     */
    public void initComponent() {


        //module.initComponent();

        tb_conceptus_cost.setModel(conceptusMaterialTableModel);


        tb_assemble_cost.setModel(assembleMaterialTableModel);

        tb_product_paint.setModel(productPaintModel);


        tb_conceptus_wage.setModel(conceptusWageTableModel);

        tb_assemble_wage.setModel(assembleWageTableModel);


        tb_pack_cost.setModel(packMaterialTableModel);

        tb_pack_wage.setModel(packWageTableModel);




        //咸康信息 默认不显示
        panel_xiankang.setVisible(false);


        int defaultRowHeight= ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;

        tb_conceptus_cost.setRowHeight(defaultRowHeight);
        tb_assemble_cost.setRowHeight(defaultRowHeight);
        tb_pack_cost.setRowHeight(defaultRowHeight);
        tb_conceptus_wage.setRowHeight(defaultRowHeight);
        tb_assemble_wage.setRowHeight(defaultRowHeight);
        tb_pack_wage.setRowHeight(defaultRowHeight);

        tb_product_paint.setRowHeight(defaultRowHeight);

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
                    JPopupMenu menu = new TablePopMenu(source, tableMenuLister);
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
        tb_pack_wage.addMouseListener(adapter);




        KeyListener listener=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getSource() instanceof  JTable) {

                    JTable table=(JTable)e.getSource();

                    //黏贴
                    if (e.isControlDown()) {

                        if (e.getKeyCode() == KeyEvent.VK_V) {

                            handleClipBordDataToTable((BaseTableModel) table.getModel(),table.convertRowIndexToModel(table.getSelectedRow()));
                        }

                    }
                }

            }
        };


        tb_conceptus_cost.addKeyListener(listener);
        tb_assemble_cost .addKeyListener(listener);
        tb_pack_cost.addKeyListener(listener);
        tb_product_paint.addKeyListener(listener);





        /**
         * 表格弹出菜单回调接口
         */
        tableMenuLister = new TablePopMenu.TableMenuLister() {
            @Override
            public void onTableMenuClick(int index, BaseTableModel tableModel, int rowIndex[]) {
                switch (index) {


                    case TablePopMenu.ITEM_INSERT:

                        tableModel.addNewRow(rowIndex[0]);

                        break;
                    case TablePopMenu.ITEM_DELETE:

                        tableModel.deleteRows(rowIndex);
                        break;
                    case TablePopMenu.ITEM_PAST:

                        handleClipBordDataToTable(tableModel, rowIndex[0]);
                        break;
                }
            }
        };



        //装箱数量 修改监听器
        tf_quantityListener=new DocumentListener() {



            @Override
            public void insertUpdate(DocumentEvent e) {



                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                       update();
                    }
                });

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


               // update();

            }



            private void update()
            {


              //  Logger.getLogger("TEST").info("updating:"+tf_quantity.getText().trim());

              int newValue=0;
                try{

                    newValue= Integer.valueOf(tf_quantity.getText().trim());

                }catch (Throwable t)
                {

                }

                productDetail.product.packQuantity=newValue;
                packMaterialTableModel.fireTableDataChanged();
                packWageTableModel.fireTableDataChanged();
            }
        };

    }


    /**
     * 处理复制黏贴数据
     *
     * @param tableModel
     * @param rowIndex
     */
    private void handleClipBordDataToTable(final BaseTableModel tableModel, final int rowIndex) {


        String clipboardText = ClipBordHelper.getSysClipboardText();


        String[] rows = clipboardText.split("[\n]+");

        final List<String> codes = new ArrayList<String>();
        final List<String> names=new ArrayList<>();
        int length = rows.length;
        final String[][] tableData = new String[length][];
        for (int i = 0; i < length; i++) {
            tableData[i] = rows[i].split("\t");



                //油漆表
                if(tableModel instanceof  ProductPaintTableModel)
                {

                    //复制数据油漆名称在第四例
                   if( tableData[i].length>3 && !StringUtils.isEmpty(tableData[i][3]))
                   {
                       names.add(tableData[i][3]);
                   }

                }

                else
                {
                    if (tableData[i].length > 0 && !StringUtils.isEmpty(tableData[i][0])) {

                        codes.add(tableData[i][0]);

                    }
                }






        }


        new HdSwingWorker<Material, Object>(getWindow(getRoot())) {


            @Override
            public void onResult(RemoteData<Material> data) {


                int firstRow = rowIndex;
                for (String[] row : tableData) {




                    Object object = tableModel.addNewRow(firstRow++);
                    if (object instanceof ProductMaterial) {



                        String code = row == null || row.length == 0 ? "" : row[0].trim();
                        if (StringUtils.isEmpty(code))
                            continue;
                        ProductMaterial productMaterial = (ProductMaterial) object;




                        for (Material material : data.datas) {


                            if (code.equals(material.code)) {
                                float quantity = 0;
                                try {
                                    quantity = FloatHelper.scale(Float.valueOf(row[2]), 4);
                                } catch (Throwable t) {

                                }
                                productMaterial.setQuantity(quantity);

                                float pLong = 0;
                                try {
                                    pLong = FloatHelper.scale(Float.valueOf(row[3]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpLong(pLong);

                                float pWidth = 0;
                                try {
                                    pWidth = FloatHelper.scale(Float.valueOf(row[4]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpWidth(pWidth);


                                float pHeight = 0;
                                try {
                                    pHeight = FloatHelper.scale(Float.valueOf(row[5]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpHeight(pHeight);


                                productMaterial.updateMaterial(material);
                                //tableModel.fireTableDataChanged();

                                break;
                            }

                        }


                    }else
                    if(object instanceof  ProductPaint){


                        ProductPaint productPaint=(ProductPaint) object;
                        String name = row == null || row.length <=3   ? "" : row[3].trim();


                        String processCode ="";
                        try {
                            processCode = row[0];
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessCode(processCode);


                        String processName ="";
                        try {
                            processName = row[1];
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessName(processName);


                        float processPrice = 0;
                        try {
                            processPrice = FloatHelper.scale(Float.valueOf(row[2]));
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessPrice(processPrice);

                        float ingredientRatio = 0;
                        try {
                            ingredientRatio = FloatHelper.scale(Float.valueOf(row[4]));
                        } catch (Throwable t) {

                        }
                        productPaint.setIngredientRatio(ingredientRatio) ;


                        float materialQuantity = 0;
                        try {
                            materialQuantity = FloatHelper.scale(Float.valueOf(row[6]));
                        } catch (Throwable t) {

                        }
                        productPaint.setMaterialQuantity(materialQuantity);

                        //查找匹配材料   油漆表中 材料不是必须
                        for (Material material : data.datas) {

                            if (name.equals(material.name)) {

                                productPaint.updateMaterial(material);


                                break;
                            }

                        }




                    }


                }
                tableModel.fireTableDataChanged();

            }

            @Override
            protected RemoteData<Material> doInBackground() throws Exception {


                 if(codes.size()>0)
                 {
                     return apiManager.readMaterialListByCodeEquals(codes);

                 }else
                     return  apiManager.readMaterialListByNameEquals(names);



            }
        }.go();


    }


    /**
     * 绑定汇总数据
     */
    private void bindStatisticsValue(Product product) {
        jtf_paint_cost.setText(String.valueOf(product.paintCost));
        jtf_paint_wage.setText(String.valueOf(product.paintWage));
        jtf_assemble_cost.setText(String.valueOf(product.assembleCost));
        jtf_assemble_wage.setText(String.valueOf(product.assembleWage));

        jtf_conceptus_cost.setText(String.valueOf(product.conceptusCost));
        jtf_conceptus_wage.setText(String.valueOf(product.conceptusWage));

        jtf_cost1.setText(String.valueOf(product.cost1));
        jtf_cost6.setText(String.valueOf(product.cost6));
        jtf_cost5.setText(String.valueOf(product.cost5));
        jtf_cost7.setText(String.valueOf(product.cost7));
        jtf_cost8.setText(String.valueOf(product.cost8));
        jtf_cost11_15.setText(String.valueOf(product.cost11_15));

        tf_unpack_cost.setText(String.valueOf(product.productCost));
        tf_cost4.setText(String.valueOf(product.cost4));
        tf_fob.setText(String.valueOf(product.fob));
        tf_price.setText(String.valueOf(product.price));
        tf_cost.setText(String.valueOf(product.cost));
        tf_volume.setText(String.valueOf(product.packVolume));
        tf_long.setText(String.valueOf(product.packLong));
        tf_width.setText(String.valueOf(product.packWidth));
        tf_height.setText(String.valueOf(product.packHeight));
        tf_inboxCount.setText(String.valueOf(product.insideBoxQuantity));





        //to avoid fire listener  first check value changed or not  and remove listener and rebind.
        if(!tf_quantity.getText().trim().equals(String.valueOf(product.packQuantity))) {
            tf_quantity.getDocument().removeDocumentListener(tf_quantityListener);
            tf_quantity.setText(String.valueOf(product.packQuantity));
            tf_quantity.getDocument().addDocumentListener(tf_quantityListener);
        }
    }

    /**
     * 配置表格的 弹出选择框
     */
    private void configTableCellEditor(final JTable table) {


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
             final   String text = ((JTextField) e.getSource()).getText().trim();
             final   int rowIndex = table.convertRowIndexToModel(table.getSelectedRow());
                final Materialable materialable;
                if (table.getModel() instanceof Materialable) {

                    materialable=       ((Materialable) table.getModel());
                }
                else
                {
                    return ;
                }
                //查询  单记录直接copy

                new HdSwingWorker<Material,Object>(SwingUtilities.getWindowAncestor(getRoot()))
                {
                    @Override
                    protected RemoteData<Material> doInBackground() throws Exception {


                        return   apiManager.loadMaterialByCodeOrName(text, 0, 100);

                    }

                    @Override
                    public void onResult(RemoteData<Material> data) {

                        if(data.isSuccess()&&data.totalCount==1)
                        {


                            materialable.setMaterial(data.datas.get(0), rowIndex);


                        }else
                        {


                            SearchMaterialDialog dialog = new SearchMaterialDialog(getWindow(contentPane), text, data);
                            dialog.setMinimumSize(new Dimension(800,600));
                            dialog.pack();
                            dialog.setLocationRelativeTo(table);
                            dialog.setVisible(true);
                            Material material = dialog.getResult();
                            if (material != null) {
                                if (table.getModel() instanceof Materialable) {

                                    ((Materialable) table.getModel()).setMaterial(material, rowIndex);
                                }

                            }

                        }




                    }
                }.go();







            }
        });

        DefaultCellEditor editor = new DefaultCellEditor(jtf);
        table.setDefaultEditor(Material.class, editor);


        JComboBox<PackMaterialType> packMaterialTypeComboBox = new JComboBox<>();
        for (PackMaterialType type : BufferData.packMaterialTypes)
            packMaterialTypeComboBox.addItem(type);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(packMaterialTypeComboBox);

        table.setDefaultEditor(PackMaterialType.class, comboboxEditor);


        JComboBox<PackMaterialPosition> packMaterialPositionComboBox = new JComboBox<>();
        for (PackMaterialPosition position : BufferData.packMaterialPositions)
            packMaterialPositionComboBox.addItem(position);
        table.setDefaultEditor(PackMaterialPosition.class, new DefaultCellEditor(packMaterialPositionComboBox));


        JComboBox<PackMaterialClass> packMaterialClassComboBox = new JComboBox<>();
        for (PackMaterialClass packMaterialClass : BufferData.packMaterialClasses)
            packMaterialClassComboBox.addItem(packMaterialClass);
        table.setDefaultEditor(PackMaterialClass.class, new DefaultCellEditor(packMaterialClassComboBox));





          DefaultTableCellRenderer renderer=new DefaultTableCellRenderer(){};


    }

    @Override
    public JComponent getRoot() {
        return contentPane;
    }

    public ProductDetail getData() {
        return productDetail;
    }
}
