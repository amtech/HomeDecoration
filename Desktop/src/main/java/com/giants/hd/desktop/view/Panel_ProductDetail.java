package com.giants.hd.desktop.view;

import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.local.*;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.utils.ExportHelper;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.CacheManager;
import com.giants.hd.desktop.dialogs.CopyProductDialog;
import com.giants.hd.desktop.dialogs.OperationLogDialog;
import com.giants.hd.desktop.interf.ComonSearch;
import com.giants.hd.desktop.interf.DataChangeListener;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.TableMouseAdapter;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.google.inject.Inject;

import jxl.write.WriteException;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
    private JTextArea ta_spec_inch;
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
    private JTextArea ta_spec_cm;
    private Panel_XK_Pack jp_pack;


    private JLabel creator;

    private JLabel updateTime;
    private JLabel updator;
    private JLabel createTime;
    private JPanel jp_log;
    private JLabel viewLog;
    private JPanel panel_nomal;
    private JButton btn_resume;
    private JPanel panel_delete;
    private JTextField jtf_cost_repair;
    private JTextField jtf_pack_wage;
    private JTextField jtf_pack_cost;


    /**
     * 旧数据 传递进来的数据
     */
    private ProductDetail oldData;

    public ProductDetail productDetail;


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
     * 规格英寸输入框的监听
     */
    private DocumentListener ta_spec_inchListener;
    /**
     * 规格厘米输入框的监听
     */
    private DocumentListener ta_spec_cmListener;


    /**
     * 所有表格表格监听对象
     */
    TableModelListener allTableModelListener;


    /**
     * 表格菜单的回调接口
     */
    private TablePopMenu.TableMenuLister tableMenuLister;


   private  DocumentListener tf_quantityListener;
    /**
     * 单位改变的监听器
     */
    private DocumentListener tf_unitDocumentListener;
    /**
     * 包装改变监听类。
     */
    private ItemListener cb_packItemListener;



    GlobalData globalData=CacheManager.getInstance().bufferData.globalData;


    public Panel_ProductDetail( ) {
    super();

        initComponent();



    }


    /**
     * 设置数据
     */
    public void setProductDetail(ProductDetail productDetail )
    {


        initPanel(productDetail);


    }

    /**
     * 设置数据
     */
    public void setProductDetail(ProductDetail productDetail ,ProductDelete productDelete)
    {


        initPanel(productDetail,productDelete);


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
    *   添加监听 监听文本变化，及表格变化。。（这些监听在开始时候需要移除 避免在初始化绑定数据时候触发监听）
     */
    private void addListeners() {

        //数量改变
         tf_quantity.getDocument().addDocumentListener(tf_quantityListener);

//        //修改规格监听
    //    ta_spec_cm.getDocument().addDocumentListener(ta_spec_cmListener);
//        //修改规格监听
      // ta_spec_inch.getDocument().addDocumentListener(ta_spec_inchListener);




    }



    public void removeListeners()
    {
        //数量改变
        tf_quantity.getDocument().removeDocumentListener(tf_quantityListener);

//        //修改规格监听
        ta_spec_cm.getDocument().removeDocumentListener(ta_spec_cmListener);
//        //修改规格监听
        ta_spec_inch.getDocument().removeDocumentListener(ta_spec_inchListener);




//
//        //注册表模型的监听
//        productPaintModel.removeTableModelListener(allTableModelListener);
//        assembleMaterialTableModel.removeTableModelListener(allTableModelListener);
//        assembleWageTableModel.removeTableModelListener(allTableModelListener);
//        conceptusMaterialTableModel.removeTableModelListener(allTableModelListener);
//        conceptusWageTableModel.removeTableModelListener(allTableModelListener);
//        packMaterialTableModel.removeTableModelListener(allTableModelListener);
//        packWageTableModel.removeTableModelListener(allTableModelListener);
    }



    private void initListeners()
    {
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
                productDetail.updateProductStatistics(globalData);
                bindStatisticsValue(productDetail.product);

            }
        };



//        //注册表模型的监听
//        productPaintModel.addTableModelListener(allTableModelListener);
//        assembleMaterialTableModel.addTableModelListener(allTableModelListener);
//        assembleWageTableModel.addTableModelListener(allTableModelListener);
//        conceptusMaterialTableModel.addTableModelListener(allTableModelListener);
//        conceptusWageTableModel.addTableModelListener(allTableModelListener);
//        packMaterialTableModel.addTableModelListener(allTableModelListener);
//        packWageTableModel.addTableModelListener(allTableModelListener);



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



                int newValue=0;
                try{

                    newValue= Integer.valueOf(tf_quantity.getText().trim());
                    productDetail.product.packQuantity=newValue;
                    packMaterialTableModel.updateProduct();
                    packWageTableModel.fireTableDataChanged();


                }catch (Throwable t)
                {

                }


            }
        };




        //英寸输入监听
        ta_spec_inchListener=new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {


                updateCm();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCm();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }



            public void updateCm()
            {


                String cmString= StringUtils.convertInchStringToCmString( ta_spec_inch.getText().trim());
                bindProductSpecCmData(cmString);


            }
        };



        //厘米输入监听
        ta_spec_cmListener=new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {


                updateInch( );
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateInch() ;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            public void updateInch( )
            {


                String inchString=StringUtils.convertCmStringToInchString(ta_spec_cm.getText().trim());

                bindProductSpecInchData(inchString);

            }
        };



        //包材选择切换箭筒
        cb_packItemListener=new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {


                if(e.getStateChange()==ItemEvent.SELECTED) {
                    productDetail.product.pack = (Pack) e.getItem();
                    packMaterialTableModel.updateProduct();
                }
            }
        };


        tf_unitDocumentListener=new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void update()
            {

                productDetail.product.pUnitName=tf_unit.getText().trim();
                packMaterialTableModel.updateProduct();

            }
        };


        /**
         * 导出数据
         */
        btn_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                JFileChooser fileChooser = new JFileChooser(".");
                //下面这句是去掉显示所有文件这个过滤器。
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                File file=null;
                if (result == JFileChooser.APPROVE_OPTION) {

                    file= fileChooser.getSelectedFile();

                }
                if(file==null) return;

                try {
                    ExportHelper.export(productDetail, file.getPath());
                    JOptionPane.showMessageDialog(getRoot(), "导出成功");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getRoot(),e1.getLocalizedMessage());
                } catch (WriteException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getRoot(), e1.getLocalizedMessage());
                }

            }
        });



        //咸康包装描述数据改变监听接口。
        jp_pack.setDataChangeListener(new DataChangeListener<Xiankang>() {
            @Override
            public void onDataChanged(Xiankang data) {

                productDetail.product.xiankang=data;
                packMaterialTableModel.updateProduct();


            }
        });

    }




    /**
     * 收集数据   将各当前数据 存入数据中
     *
     * @see
     */
    private void collectionData()  {


        Product product = productDetail.product;

        //货号
        String productName = tf_product.getText().trim();


        product.setName(productName);

        //规格
        String ta_spcValue = ta_spec_inch.getText().trim();
        product.setSpec(ta_spcValue);


        //规格
        String ta_spc_cmValue = ta_spec_cm.getText().trim();
        product.setSpecCm(ta_spc_cmValue);

        //备注
        String ta_memoValue = ta_memo.getText().trim();
        product.setMemo(ta_memoValue);

        //产品分类
        PClass pClassData = (PClass) cb_class.getSelectedItem();
        if(pClassData!=null) {
            product.setpClassId(pClassData.id);
            product.setpClassName(pClassData.name);
        }


        //包装类型
        Pack pack= (Pack) cb_pack.getSelectedItem();
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
        product.isXK=isXiangkang;

         product.xiankang=    panel_xiankang.getData();




        //产品净重

        try {
            float tf_weightValue = Float.valueOf(tf_weight.getText().trim());
            product.setWeight(tf_weightValue);
        } catch (Throwable t) {
           // t.printStackTrace();
            product.setWeight(0);
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

        List<ProductPaint> paints = productPaintModel.getValuableDatas();
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


        List<ProductMaterial> conceptusMaterial = conceptusMaterialTableModel.getValuableDatas();
        //TODO  白胚材料的 数据检验


        productDetail.conceptusMaterials = conceptusMaterial;


        List<ProductMaterial> assembleMaterials = assembleMaterialTableModel.getValuableDatas();
        //TODO  组装材料的 数据检验


        productDetail.assembleMaterials = assembleMaterials;


        List<ProductWage> assembleWages = assembleWageTableModel.getValuableDatas();
        //TODO  组装工资的 数据检验


        productDetail.assembleWages = assembleWages;


        List<ProductWage> conceptusWages = conceptusWageTableModel.getValuableDatas();
        //TODO  白胚工资的 数据检验


        productDetail.conceptusWages = conceptusWages;


        /////////////包装

        List<ProductMaterial> packMaterials = packMaterialTableModel.getValuableDatas();
        //TODO  包装材料 数据检验


        productDetail.packMaterials = packMaterials;


        List<ProductWage> packWages = packWageTableModel.getValuableDatas();
        //TODO   包装工资  数据检验


        productDetail.packWages = packWages;


    }




    private void checkData(ProductDetail productDetail) throws HdUIException
    {

        //检验输入

        if (StringUtils.isEmpty(productDetail.product.name)) {
            throw HdUIException.create(tf_product, "请输入货号。。。");
        }

        if(productDetail.product.pack==null)
        {
            throw HdUIException.create(cb_pack, "请选择包装类型。。。");
        }

//        if(StringUtils.isEmpty()productDetail.product.classid==null)
//        {
//            throw HdUIException.create(cb_class, "请选择产品类型。。。");
//        }

    }

    /**
     * 更新界面方法  负责数据的绑定
     */
    private void bindData() {


        Product product = productDetail.product;

        bindProductBaseInfo(product);

        bindStatisticsValue(product);


        bindTableDatas(tb_assemble_cost, assembleMaterialTableModel, productDetail.assembleMaterials);
        bindTableDatas(tb_assemble_wage,assembleWageTableModel, productDetail.assembleWages);

        bindTableDatas(tb_conceptus_cost,conceptusMaterialTableModel, productDetail.conceptusMaterials);
        bindTableDatas(tb_conceptus_wage, conceptusWageTableModel, productDetail.conceptusWages);

        bindTableDatas(tb_product_paint, productPaintModel, productDetail.paints);


        bindTableDatas(tb_pack_cost, packMaterialTableModel, productDetail.packMaterials);
        bindTableDatas(tb_pack_wage, packWageTableModel, productDetail.packWages);



    }


    /**
     * 重新表格绑定数据
     *
     * @param model
     * @param datas
     * @param <T>
     */
    private <T> void bindTableDatas(JTable table,BaseTableModel<T> model, List<T> datas) {
        //为了避免触发监听，先移除后添加
        model.removeTableModelListener(allTableModelListener);
        model.setDatas(datas);
         table.setModel(model);
         model.addTableModelListener(allTableModelListener);

    }


    /**
     * \
     * 绑定产品的基本信息   基本信息+包装成本
     *
     * @param product
     */
    private void bindProductBaseInfo(Product product) {






        if(product==null) return;

        tf_product.setText(product == null ? "" : product.getName());


        //产品 版本号 一旦保存就不能再修改
        tf_product.setEditable(product.id<=0);
        tf_version.setEditable(product.id <= 0);

        jtf_mirror_size.setText(product == null ? "" : product.mirrorSize);



        photo.setIcon(product.photo == null ? null : new ImageIcon(product.photo));

        photo.setText(product == null ? "产品图片" : "");



        bindProductSpecInchData(product == null ? "" : product.getSpec());
        bindProductSpecCmData(product == null ? "" : product.getSpecCm());

        ta_memo.setText(product == null ? "" : product.getMemo());
        date.getJFormattedTextField().setText(product == null ? "" : product.getrDate());



        tf_unit.getDocument().removeDocumentListener(tf_unitDocumentListener);
        tf_unit.setText(product == null ? "S/1" : product.pUnitName);
        tf_unit.getDocument().addDocumentListener(tf_unitDocumentListener);

        tf_weight.setText(product.getWeight() > 0.0f ? String.valueOf(product.getWeight() ): "");

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

        cb_pack.removeItemListener(cb_packItemListener);
        if(product.pack!=null)
        cb_pack.setSelectedItem(product.pack);
        else
            cb_pack.setSelectedIndex(0);
        cb_pack.addItemListener(cb_packItemListener);

        boolean isXiangkang = product != null && product.isXK  ;


        cb_xiankang.setSelected(isXiangkang);
        panel_xiankang.setVisible(isXiangkang);
        jp_pack.setVisible(isXiangkang);
         panel_xiankang.setData(product.xiankang);
          jp_pack.setData(product.xiankang);








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


        new HdSwingWorker<ProductDetail, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {

                return apiManager.saveProduct(product);


            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {

                if (data.isSuccess()) {
                    //TODO 显示保存成功
                    JOptionPane.showMessageDialog(contentPane, "数据保存成功!");

//                    if(listener!=null)
//                    {
//                        listener.close();
//                    }
                    initPanel(data.datas.get(0));


                }else
                {
                    JOptionPane.showMessageDialog(contentPane,data.message);
                }
            }
        }.go();


    }



    /**
     * 数据初始化 产品详界面
     *
     * @param detail
     */

    private void initPanel(ProductDetail detail) {

        initPanel(detail,null);
    }

    private void initPanel(ProductDetail detail,final ProductDelete productDelete) {

        removeListeners();

        if(detail==null)
        {

            productDetail=new ProductDetail();

            oldData=(ProductDetail) ObjectUtils.deepCopy(productDetail);

        }else
        {
             oldData= (ProductDetail) ObjectUtils.deepCopy(detail);
            productDetail = detail;

        }
        setProductInfoToPackageModel();
        bindData();



        bindLogData(detail == null ? null : detail.productLog);



        //非删除数据 添加对数据的监听。
        if(productDelete!=null)
          addListeners();


        panel_nomal.setVisible(null==productDelete);
        panel_delete.setVisible(productDelete != null);

        for(ActionListener listener:btn_resume.getActionListeners())
        {
            btn_resume.removeActionListener(listener);
        }
        if(productDelete!=null)
        btn_resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {




                new HdSwingWorker<ProductDetail, Void>(SwingUtilities.getWindowAncestor(Panel_ProductDetail.this.getRoot())) {
                    @Override
                    protected RemoteData<ProductDetail> doInBackground() throws Exception {
                        return apiManager.resumeDeleteProduct(productDelete.id);
                    }

                    @Override
                    public void onResult(RemoteData<ProductDetail> data) {


                        if(data.isSuccess())
                        {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()),"数据恢复成功");
                            if(listener!=null)
                            listener.close();
                        }else{

                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()),data.message);
                        }


                    }
                }.go();


                }
        });

    }


    /**
     * 绑定修改记录信息
     * @param productLog
     */
    private void bindLogData(ProductLog productLog) {


        jp_log.setVisible(productLog != null);


        if(productLog==null)
        {
            return;
        }


        creator.setText(productLog.creatorCName);
        createTime.setText(productLog.createTimeString);
        updateTime.setText(productLog.updateTimeString);
        updator.setText(productLog.updatorCName);



    }


    private void createUIComponents() {

       // cellPanel = new APanel();
        //cellPanel.setGridColor(Color.GRAY);
        // cellPanel.setPaintInBackground(false);
        cb_class = new JComboBox<PClass>();
        for (PClass pClass : CacheManager.getInstance().bufferData.pClasses) {
            cb_class.addItem(pClass);
        }

        cb_pack=  new JComboBox<Pack>();
        for (Pack pack : CacheManager.getInstance().bufferData.packs) {
            cb_pack.addItem(pack);
        }

     //  cb_pack.setSelectedIndex(0);


        JDatePanelImpl picker = new JDatePanelImpl(null);
        date = new JDatePickerImpl(picker, new HdDateComponentFormatter());



    }


    /**
     * 初始化控件装配。
     */
    public void initComponent() {





        panel_delete.setVisible(false);
        panel_nomal.setVisible(false);



        //咸康信息 默认不显示
        panel_xiankang.setVisible(false);

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




                        try {
                            tableModel.insertNewRows(TableDuplicateHelper.getBufferData(), rowIndex.length == 0 ? 0 : rowIndex[0]);
                            TableDuplicateHelper.clear();
                        }catch(Throwable t)
                        {
                            t.printStackTrace();
                            JOptionPane.showMessageDialog(getRoot(),"数据格式不对， 不能黏贴。");
                        }



                       // handleCopyTableData(tableModel, rowIndex[0]);
                        break;

                    case TablePopMenu.ITEM_COPY:

                        ObjectUtils.deepCopy(tableModel.getValuableDatas());
                        TableDuplicateHelper.saveBufferData((List<Object>) ObjectUtils.deepCopy(tableModel.getValuableDatas()));
//                        JOptionPane.showMessageDialog(getRoot(),"成功");
                       // handleClipBordDataToTable(tableModel, rowIndex[0]);
                        break;
                }
            }
        };


        TableMouseAdapter adapter=new TableMouseAdapter(tableMenuLister);
        //设置表格点击监听
        tb_conceptus_cost.addMouseListener(adapter);
        tb_conceptus_wage.addMouseListener(adapter);

        tb_assemble_cost.addMouseListener(adapter);
        tb_assemble_wage.addMouseListener(adapter);
        tb_product_paint.addMouseListener(adapter);
        tb_pack_cost.addMouseListener(adapter);
        tb_pack_wage.addMouseListener(adapter);




        KeyListener keyListener=new KeyAdapter() {
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


        tb_conceptus_cost.addKeyListener(keyListener);
        tb_assemble_cost .addKeyListener(keyListener);
        tb_pack_cost.addKeyListener(keyListener);
        tb_product_paint.addKeyListener(keyListener);










        /**
         * 保存功能
         */
        bn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    //非新增数据
                    if (!isModified()) {

                        if (productDetail.product.id > 0) {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()), "数据无改变！");
                            return;
                        }
                    }


                    checkData(productDetail);


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


                if (productDetail.product.id <= 0) {

                    JOptionPane.showMessageDialog((Component) e.getSource(), "产品数据未建立，请先保存");
                    return;

                }





                    if(isModified())
                    {
                        JOptionPane.showMessageDialog((Component) e.getSource(), "产品数据有改动，请先保存");
                        return;
                    }


                //显示对话框


                CopyProductDialog dialog = new CopyProductDialog(SwingUtilities.getWindowAncestor(getRoot()), productDetail.product);
                dialog.pack();
                dialog.setMinimumSize(new Dimension(400, 300));
                dialog.setLocationByPlatform(true);
                dialog.setModal(true);
                dialog.setVisible(true);

                if (dialog.getResult() != null)
                    HdSwingUtils.showDetailPanel(getWindow(getRoot()), dialog.getResult());


            }
        });






        /**
         * 删除
         */
        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (listener != null) listener.delete();


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
                jp_pack.setVisible(cb_xiankang.isSelected());
            }
        });


        //配置表格的自定义编辑输入   //材料
        configTableCellMaterialEditor(tb_conceptus_cost);
        configTableCellMaterialEditor(tb_assemble_cost);
        configTableCellMaterialEditor(tb_product_paint);
        configTableCellMaterialEditor(tb_pack_cost);

        configTableCellMaterialEditor(tb_pack_wage);
        configTableCellMaterialEditor(tb_conceptus_wage);
        configTableCellMaterialEditor(tb_assemble_wage);




        //配置权限  是否修改  是否可以删除

        boolean modifiable= AuthorityUtil.getInstance().editProduct()||AuthorityUtil.getInstance().addProduct();

        bn_save.setVisible(modifiable);


        btn_copy.setVisible(AuthorityUtil.getInstance().editProduct());


        btn_delete.setVisible(AuthorityUtil.getInstance().deleteProduct());


        btn_export.setVisible(AuthorityUtil.getInstance().exportProduct());



        viewLog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2)
                {



                    Window window=getWindow(Panel_ProductDetail.this.getRoot());
                    OperationLogDialog dialog=new OperationLogDialog(window,Product.class,productDetail.product.id);
                    dialog.setLocationRelativeTo(window);
                    dialog.setVisible(true);


                }
            }
        });


        //配置表格的自定义编辑输入   //工序


        initListeners();
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
                        productPaint.ingredientRatio=ingredientRatio; ;


                        float materialQuantity = 0;
                        try {
                            materialQuantity = FloatHelper.scale(Float.valueOf(row[6]));
                        } catch (Throwable t) {

                        }
                        productPaint.quantity=materialQuantity;

                        //查找匹配材料   油漆表中 材料不是必须
                        for (Material material : data.datas) {

                            if (name.equals(material.name)) {

                                productPaint.updateMaterial(material,globalData);


                                break;
                            }

                        }

                        productPaint.updatePriceAndCostAndQuantity(globalData);




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
        if(product==null) return;
        jtf_paint_cost.setText(String.valueOf(product.paintCost));
        jtf_paint_wage.setText(String.valueOf(product.paintWage));
        jtf_assemble_cost.setText(String.valueOf(product.assembleCost));
        jtf_assemble_wage.setText(String.valueOf(product.assembleWage));

        jtf_conceptus_cost.setText(String.valueOf(product.conceptusCost));
        jtf_conceptus_wage.setText(String.valueOf(product.conceptusWage));

        jtf_pack_cost.setText(String.valueOf(product.packCost));
        jtf_pack_wage.setText(String.valueOf(product.packWage));

        jtf_cost1.setText(String.valueOf(product.cost1));
        jtf_cost6.setText(String.valueOf(product.cost6));
        jtf_cost5.setText(String.valueOf(product.cost5));
        jtf_cost7.setText(String.valueOf(product.cost7));
        jtf_cost8.setText(String.valueOf(product.cost8));
        jtf_cost11_15.setText(String.valueOf(product.cost11_15));

        jtf_cost_repair.setText(String.valueOf(product.repairCost));

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
     * 绑定英寸规格数据
     * @param inchString
     */
    private void bindProductSpecInchData(String inchString)
    {


        ta_spec_inch.getDocument().removeDocumentListener(ta_spec_inchListener);
        ta_spec_inch.setText(inchString);
        ta_spec_inch.getDocument().addDocumentListener(ta_spec_inchListener);

    }


    /**
     * 绑定厘米规格数据
     * @param cmString
     */
    private void bindProductSpecCmData(String cmString)
    {


        ta_spec_cm.getDocument().removeDocumentListener(ta_spec_cmListener);
        ta_spec_cm.setText(cmString);
        ta_spec_cm.getDocument().addDocumentListener(ta_spec_cmListener);

    }

    /**
     * 配置表格的 弹出选择框  材料选择
     */
    private void configTableCellMaterialEditor(final JTable table) {


        //定制表格的编辑功能 弹出物料选择单

        final JTextField jtf = new JTextField();
        jtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });



        jtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                Logger.getLogger("TAG").info("focusLost" + e.toString());
                handleTableMaterialInput(table, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf);
        table.setDefaultEditor(Material.class, editor);





        final JTextField processjtf = new JTextField();
        processjtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!processjtf.hasFocus())
                    processjtf.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!processjtf.hasFocus())
                    processjtf.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });



        processjtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                handleTableProcessInput(table, text);

            }
        });



        table.setDefaultEditor(ProductProcess.class, new DefaultCellEditor(processjtf));


        JComboBox<PackMaterialType> packMaterialTypeComboBox = new JComboBox<>();
        for (PackMaterialType type : CacheManager.getInstance().bufferData.packMaterialTypes)
            packMaterialTypeComboBox.addItem(type);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(packMaterialTypeComboBox);

        table.setDefaultEditor(PackMaterialType.class, comboboxEditor);


        JComboBox<PackMaterialPosition> packMaterialPositionComboBox = new JComboBox<>();
        for (PackMaterialPosition position : CacheManager.getInstance().bufferData.packMaterialPositions)
            packMaterialPositionComboBox.addItem(position);
        table.setDefaultEditor(PackMaterialPosition.class, new DefaultCellEditor(packMaterialPositionComboBox));


        JComboBox<PackMaterialClass> packMaterialClassComboBox = new JComboBox<>();
        for (PackMaterialClass packMaterialClass : CacheManager.getInstance().bufferData.packMaterialClasses)
            packMaterialClassComboBox.addItem(packMaterialClass);
        table.setDefaultEditor(PackMaterialClass.class, new DefaultCellEditor(packMaterialClassComboBox));





        //让浮点数去除尾吧0
         DefaultTableCellRenderer renderer=new DefaultTableCellRenderer(){
            private DecimalFormat format=new DecimalFormat("#.#####");
             @Override
             protected void setValue(Object value) {
                 if(value instanceof  Float) {

                    super.setValue(format.format(((Float) value).floatValue()));
                    // super.setValue( FloatHelper.scale(((Float) value).floatValue(), 10));
                 }else
                 super.setValue(value);
             }
         };

        table.setDefaultRenderer( Object.class,renderer);


    }
    /**
     * 配置表格的 弹出选择框  工序选择
     */
    private void configTableCellProcessEditor(final JTable table) {


        //定制表格的编辑功能 弹出物料选择单










    }

    /**
     * 处理表格的 材料输入时间
     * @return
     * @param table
     * @param text
     */


    private void  handleTableMaterialInput(final JTable table,final  String text)
    {

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



                    SearchDialog<Material> dialog = new SearchDialog.Builder().setWindow(getWindow(contentPane)).setTableModel(new MaterialTableModel()).setComonSearch(new ComonSearch<Material>() {
                        @Override
                        public RemoteData<Material> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.loadMaterialByCodeOrName(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
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


    /**
     * 处理工序输入检索
     * @param table
     * @param text
     */
    private  void handleTableProcessInput(final JTable table,final String text)
    {






        final   int rowIndex = table.convertRowIndexToModel(table.getSelectedRow());
        final Processable processable;
        if (table.getModel() instanceof Processable) {

            processable=       ((Processable) table.getModel());
        }
        else
        {
            return ;
        }
        //查询  单记录直接copy
        new HdSwingWorker<ProductProcess,Object>(SwingUtilities.getWindowAncestor(getRoot()))
        {
            @Override
            protected RemoteData<ProductProcess> doInBackground() throws Exception {


                return   apiManager.loadProcessByCodeOrName(text, 0, 100);

            }

            @Override
            public void onResult(RemoteData<ProductProcess> data) {

                if(data.isSuccess()&&data.totalCount==1)
                {

                    processable.setProcess(data.datas.get(0), rowIndex);


                }else
                {


                    SearchDialog<ProductProcess> dialog = new SearchDialog.Builder().setWindow(getWindow(contentPane)).setTableModel(new ProductProcessModel(false)).setComonSearch(new ComonSearch<ProductProcess>() {
                        @Override
                        public RemoteData<ProductProcess> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.loadProcessByCodeOrName(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
                    dialog.pack();
                    dialog.setLocationRelativeTo(table);
                    dialog.setVisible(true);
                    ProductProcess process = dialog.getResult();
                    if (process != null) {
                        if (table.getModel() instanceof Processable) {


                            ((Processable) table.getModel()).setProcess(process, rowIndex);
                        }

                    }

                }




            }
        }.go();












    }


    @Override
    public JComponent getRoot() {
        return contentPane;
    }

    public ProductDetail getData() {
        return productDetail;
    }



    /**
     * 预留方法
     * @param data
     */
    public void getData(ProductDetail data) {
    }

    /**
     *
     *
     * @return
     */
    public boolean isModified( )   {




            collectionData();


       return !productDetail.equals(oldData);

    }


    public void setData(Xiankang data) {
    }

    public void getData(Xiankang data) {
    }

    public boolean isModified(Xiankang data) {
        return false;
    }
}