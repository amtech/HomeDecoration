package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.dialogs.ExportQuotationDialog;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.presenter.QuotationDetailPresenter;
import com.giants.hd.desktop.view.QuotationDetailView;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.ImageViewDialog;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants.hd.desktop.dialogs.OperationLogDialog;
import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.interf.ComonSearch;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.QuotationItemPopMenu;
import com.giants.hd.desktop.widget.header.ColumnGroup;
import com.giants.hd.desktop.widget.header.GroupableTableHeader;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.Product2;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * 报价单详情
 *
 * Created by davidleen29 on 2015/6/30.
 */
public class Panel_QuotationDetail extends BasePanel  implements QuotationDetailView{
    private JHdTable tb;
    private JPanel root;
    private JTextArea ta_memo;
    private JTextField jtf_number;
    private JComboBox cb_currency;
    private JComboBox<User> cb_salesman;
    private JComboBox<Customer> cb_customer;
    private JDatePickerImpl qDate;
    private JDatePickerImpl vDate;
    private JButton btn_save;
    private JButton btn_delete;
    private JButton btn_export;
    private JLabel creator;
    private JLabel createTime;
    private JPanel jp_log;
    private JLabel updator;
    private JLabel updateTime;
    private JLabel viewLog;
    private JButton btn_resume;
    private JButton btn_verify;
    private JLabel icon_verify;
    private JButton btn_unVerify;
    private JPanel jp_verify;
    private JTextField jtf_company;
    private JLabel lb_company;
    private JButton btn_reimport;
    private JLabel icon_overdue;
    public QuotationDetail data;


    @Inject
    ApiManager apiManager;



    QuotationItemTableModel model;

    QuotationItemXKTableModel xkModel;




    QuotationDetailPresenter presenter;



    @Inject
    public Panel_QuotationDetail(QuotationDetailPresenter presenter) {


        init();
        this.presenter=presenter;
    }

    private void init() {
        model=new QuotationItemTableModel();
        xkModel=new QuotationItemXKTableModel();



     //   tb.setModel(model);


        configTableEditor();
        configProduct2Editor();


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

              presenter.save();
            }
        });


        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if (e.getClickCount() == 2) {


                    int column = tb.convertColumnIndexToModel(tb.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 1) {

                       Object object= ((BaseTableModel<Object>)tb.getModel()).getItem(tb.convertRowIndexToModel(tb.getSelectedRow()));

                        if(object instanceof QuotationItem) {

                            QuotationItem item =  (QuotationItem)object;
                            if (item.productId > 0) {
                                ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName, item.pVersion, item.photoUrl);
                            }
                        }else
                        {
                            QuotationXKItem item =  (QuotationXKItem)object;
                            if (item.productId > 0) {
                                ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName, item.pVersion, item.photoUrl);
                            }
                        }

                    }

                }

            }
        });



        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.delete();


            }
        });




        btn_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


               new ExportQuotationDialog(SwingUtilities.getWindowAncestor(getRoot()),data).setVisible(true);



            }
        });



        //配置权限  是否修改  是否可以删除

        boolean modifiable= AuthorityUtil.getInstance().editQuotation()||AuthorityUtil.getInstance().addQuotation();

        btn_save.setVisible(modifiable);




        btn_delete.setVisible(AuthorityUtil.getInstance().deleteQuotation());


        btn_export.setVisible(AuthorityUtil.getInstance().exportQuotation());



        viewLog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    Window window = getWindow(Panel_QuotationDetail.this.getRoot());
                    OperationLogDialog dialog = new OperationLogDialog(window, Quotation.class, data.quotation.id);
                    dialog.setLocationRelativeTo(window);
                    dialog.setVisible(true);


                }

            }
        });





        //取消审核功能
        btn_unVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.unVerify();


            }
        });

        //审核功能
        btn_verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.verify();


            }
        });


        cb_customer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Customer customer = (Customer) e.getItem();
                    jtf_company.setVisible(customer.code.equals(Customer.CODE_TEMP));
                    lb_company.setVisible(customer.code.equals(Customer.CODE_TEMP));
                    getWindow(jtf_company).pack();

                }


            }
        });


        //恢复功能默认不显示
        btn_resume.setVisible(false);

        //审核状态默认不显示
        icon_verify.setVisible(false);
        jp_verify.setVisible(false);

        //过期状态默认不显示
        icon_overdue.setVisible(false);


        //重新导入
        btn_reimport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {




            final    Set<Long> productIds=new HashSet<Long>();

                for(QuotationItem item :model.getValuableDatas())
                {
                    productIds.add(item.productId);
                }
                for(QuotationXKItem item :xkModel.getValuableDatas())
                {
                    productIds.add(item.productId);
                    productIds.add(item.productId2);
                }

                new HdSwingWorker<Product, Object>(getWindow(root)) {

                    @Override
                    protected RemoteData<Product> doInBackground() throws Exception {

                     return   apiManager.readProductsByIds(productIds);

                    }

                    @Override
                    public void onResult(RemoteData<Product> data) {



                        if(data.isSuccess())
                        {
                            java.util.List<Product> productList=data.datas;



                            for(QuotationItem item :model.getValuableDatas())
                            {

                              Product product=  findProduct(productList,item.productId );
                                if(product!=null)
                                {
                                    item.updateProduct(product);
                                }


                            }
                            //更新值
                            for(QuotationXKItem item :xkModel.getValuableDatas())
                            {
                                Product product=  findProduct(productList,item.productId );


                                Product product2=  findProduct(productList,item.productId2 );
                                if(product!=null)
                                {
                                    item.updateProduct(product,product2);
                                }

                            }


                            //数据源改变

                            model.fireTableDataChanged();
                            xkModel.fireTableDataChanged();


                        }else
                        {
                            JOptionPane.showMessageDialog(getWindow(),data.message);
                        }







                    }
                }.execute();





            }
        });





        tb.addMouseListener(new MouseAdapter() {

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

                    JPopupMenu menu = new QuotationItemPopMenu(tb, new QuotationItemPopMenu.TableMenuLister() {
                        @Override
                        public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {

                            switch (index) {


                                case QuotationItemPopMenu.ITEM_INSERT:

                                    tableModel.addNewRow(rowIndex[0]);

                                    break;
                                case QuotationItemPopMenu.ITEM_DELETE:

                                    tableModel.deleteRows(rowIndex);
                                    break;
                                case QuotationItemPopMenu.ITEM_APPEND:
                                    tableModel.appendRows(10);

                            }

                        }
                    });


                    menu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        });
    }



    private Product findProduct(List<Product> products,long id)
    {
        for(Product product:products)
        {
            if(product.id==id)
            {
                return product;
            }

        }
        return null;
    }


    private void configTableEditor()
    {


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
                handleTableProductInput(tb, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf);
        tb.setDefaultEditor(Product.class, editor);
    }



    private void configProduct2Editor()
    {

        //定制表格的编辑功能 弹出物料选择单

        final JTextField jtf_product2 = new JTextField();
        jtf_product2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf_product2.hasFocus())
                    jtf_product2.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf_product2.hasFocus())
                    jtf_product2.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });



        jtf_product2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                handleTableProduct2Input(tb, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf_product2);
        tb.setDefaultEditor(Product2.class, editor);
    }


    /**
     * 处理表格的产品搜索
     * @param tb
     * @param text
     */
    private void handleTableProductInput(final JHdTable tb, final String text) {




        if(!(tb.getModel() instanceof Productable))
        {
            return;
        }
        final Productable productable= (Productable) tb.getModel();

        final   int rowIndex = tb.convertRowIndexToModel(tb.getSelectedRow());
        //查询  单记录直接copy
        new HdSwingWorker<Product,Object>(SwingUtilities.getWindowAncestor(getRoot()))
        {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {


                return   apiManager.readProductList(text, 0, 100);

            }

            @Override
            public void onResult(RemoteData<Product> data) {

                if(data.isSuccess() && data.totalCount == 1) {

                    productable.setProduct(data.datas.get(0), rowIndex);


                }else
                {



                    SearchDialog<Product> dialog = new SearchDialog.Builder().setWindow(getWindow(root)).setTableModel(new ProductTableModel()).setComonSearch(new ComonSearch<Product>() {
                        @Override
                        public RemoteData<Product> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.readProductList(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
                    dialog.pack();
                    dialog.setLocationRelativeTo(tb);
                    dialog.setVisible(true);
                    Product product = dialog.getResult();
                    if (product != null) {
                        productable.setProduct(product, rowIndex);

                    }


                }




            }
        }.go();

    }


    /**
     * 处理表格的产品搜索
     * @param tb
     * @param text
     */
    private void handleTableProduct2Input(final JHdTable tb, final String text) {
        if(!(tb.getModel() instanceof QuotationItemXKTableModel))
        {
            return;
        }
        final QuotationItemXKTableModel productable= (QuotationItemXKTableModel) tb.getModel();

        final   int rowIndex = tb.convertRowIndexToModel(tb.getSelectedRow());
        final  QuotationXKItem item=productable.getItem(rowIndex);
        if(item.productId<=0)
        {
            JOptionPane.showMessageDialog(getRoot(),"请先挑选品名");
            return;
        }
        //查询  单记录直接copy
        new HdSwingWorker<Product,Object>(SwingUtilities.getWindowAncestor(getRoot()))
        {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {

                return    apiManager.readSameNameProductList(item.productName, item.productId)  ;

            }

            @Override
            public void onResult(RemoteData<Product> data) {

                if(data.isSuccess() && data.totalCount == 1) {

                    productable.setProduct2(data.datas.get(0), rowIndex);

                }else
                {



                    SearchDialog<Product> dialog = new SearchDialog.Builder().setWindow(getWindow(root)).setTableModel(new ProductTableModel()).setComonSearch(new ComonSearch<Product>() {
                        @Override
                        public RemoteData<Product> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.readProductList(value, pageIndex, pageCount);
                        }
                    }).setValue(item.productName).setSearchTextFixed(true).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
                    dialog.pack();
                    dialog.setLocationRelativeTo(tb);
                    dialog.setVisible(true);
                    Product product = dialog.getResult();
                    if (product != null) {
                        productable.setProduct2(product, rowIndex);

                    }


                }




            }
        }.go();

    }

    @Override
    public JComponent getRoot() {
        return root;
    }


    public void setQuotationDelete(final QuotationDelete quotationDelete)
    {


        if(quotationDelete!=null) {

            btn_save.setVisible(false);
            btn_delete.setVisible(false);
            btn_export.setVisible(false);
            btn_verify.setVisible(false);
            btn_unVerify.setVisible(false);
            btn_resume.setVisible(true);
            btn_resume.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    new HdSwingWorker<QuotationDetail, Void>(SwingUtilities.getWindowAncestor(getRoot())) {
                        @Override
                        protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                            return apiManager.resumeDeleteQuotation(quotationDelete.id);
                        }

                        @Override
                        public void onResult(RemoteData<QuotationDetail> data) {


                            if (data.isSuccess()) {
                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), "数据恢复成功");
                                if (listener != null)
                                    listener.close();
                            } else {

                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), data.message);
                            }


                        }
                    }.go();


                }
            });
        }


    }

    public void setData(QuotationDetail data ) {


        this.data=data;




        Quotation quotation=data.quotation;
        switch ((int)quotation.quotationTypeId)
        {

            case Quotation.QUOTATION_TYPE_NORMAL:

                tb.setModel(model);
                break;
            case Quotation.QUOTATION_TYPE_XK:

                tb.setModel(xkModel);
                TableColumnModel cm = tb.getColumnModel();
                ColumnGroup g_name = new ColumnGroup("普通包装");
                int startIndex=5;
                for(int i=0;i<12;i++)
                    g_name.add(cm.getColumn(startIndex+i));
                ColumnGroup g_lang = new ColumnGroup("加强包装");
                startIndex=17;
                for(int i=0;i<12;i++)
                    g_lang.add(cm.getColumn(startIndex+i));

                GroupableTableHeader header = (GroupableTableHeader)tb.getTableHeader();
                header.addColumnGroup(g_name);
                header.addColumnGroup(g_lang);
                break;
        }




        jtf_number.setText(quotation.qNumber);
        qDate.getJFormattedTextField().setText(quotation.qDate);
        vDate.getJFormattedTextField().setText(quotation.vDate);
        jtf_company.setText(quotation.companyName);

        int index=-1;

            for (int i = 0, count = cb_customer.getItemCount(); i < count; i++) {

                if (cb_customer.getItemAt(i).id == quotation.customerId) {

                    index = i;
                    break;
                }

            }

        cb_customer.setSelectedIndex(index
        );

         index=-1;

        for (int i = 0, count = cb_salesman.getItemCount(); i < count; i++) {

            if (cb_salesman.getItemAt(i).id == quotation.salesmanId) {

                index = i;
                break;
            }

        }

        cb_salesman.setSelectedIndex(index
        );


        for (int i = 0, count = cb_currency.getItemCount(); i < count; i++) {

            if (cb_currency.getItemAt(i).equals(  quotation.currency)) {

                index = i;
                break;
            }

        }
        cb_currency.setSelectedIndex(index);

        ta_memo.setText(quotation.memo);

        model.setDatas(data.items);
        xkModel.setDatas(data.XKItems);






        jp_verify.setVisible(true);
        icon_verify.setVisible(data.quotation.isVerified);


        icon_overdue.setVisible(data.quotation.isOverdue());


        boolean isVerifiable=AuthorityUtil.getInstance().verifyQuotation();
        boolean isEditable=AuthorityUtil.getInstance().editQuotation();


        //非审核状态方可编辑
       // btn_save.setEnabled(!data.quotation.isVerified);

        //btn_delete.setEnabled(!data.quotation.isVerified);

        //有编辑没有审核权限才可看见，
       // btn_save.setVisible(!isVerifiable && isEditable);

        //btn_delete.setVisible(!isVerifiable && isEditable);



        btn_verify.setVisible(isVerifiable);
        btn_unVerify.setVisible(isVerifiable);
        btn_export.setVisible(AuthorityUtil.getInstance().exportQuotation());



        //未审核 撤销审核按钮不可用
        btn_unVerify.setEnabled(data.quotation.isVerified);
        //审核后方可导出
        //btn_export.setEnabled(data.quotation.isVerified);

        bindLogData(data.quotationLog);



    }



    public void checkData(QuotationDetail detail)throws HdUIException
    {


        if(StringUtils.isEmpty(jtf_number.getText().trim()))
        {
            throw   HdUIException.create(jtf_number,"请输入报价单号");
        }

        if(cb_customer.getSelectedItem()==null)
        {
            throw   HdUIException.create(cb_customer,"请选择客户");
        }


        if(cb_salesman.getSelectedItem()==null)
        {
            throw   HdUIException.create(cb_salesman,"请选择业务员");
        }
        if(cb_currency.getSelectedItem()==null)
        {
            throw   HdUIException.create(cb_currency,"请选择币种");
        }

        if(StringUtils.isEmpty(qDate.getJFormattedTextField().getText()) )
        {
            throw   HdUIException.create(qDate,"请选择报价日期");
        }
        if(StringUtils.isEmpty(vDate.getJFormattedTextField().getText()) )
        {
            throw   HdUIException.create(vDate,"请选择有效日期");
        }




    }

    public void getData(QuotationDetail data) {

        Quotation quotation=data.quotation;

        quotation.qNumber=jtf_number.getText().trim();
        quotation.currency=String.valueOf(cb_currency.getSelectedItem()==null?"":cb_currency.getSelectedItem()) ;
        Customer selectedCustomer= (Customer) cb_customer.getSelectedItem();
        if(selectedCustomer!=null) {
            quotation.customerId = selectedCustomer.id;
            quotation.customerCode=selectedCustomer.code;
            quotation.customerName = selectedCustomer.name;
        }
        User selectedSalesman= (User) cb_salesman.getSelectedItem();
        if(selectedSalesman!=null) {
            quotation.salesmanId = selectedSalesman.id;
            quotation.salesman = selectedSalesman.chineseName;
        }
        quotation.qDate=qDate.getJFormattedTextField().getText().trim();
        quotation.vDate=vDate.getJFormattedTextField().getText().trim();
        quotation.memo=ta_memo.getText();
        quotation.companyName=jtf_company.getText();
        data.items.clear();
        data.items.addAll(model.getValuableDatas());
        data.XKItems.clear();
        data.XKItems.addAll(xkModel.getValuableDatas());


    }



    /**
     * 绑定修改记录信息
     * @param quotationLog
     */
    private void bindLogData(QuotationLog quotationLog) {


        jp_log.setVisible(quotationLog!=null);


        if(quotationLog==null)
        {
            return;
        }


        creator.setText(quotationLog.creatorCName);
        createTime.setText(quotationLog.createTimeString);
        updateTime.setText(quotationLog.updateTimeString);
        updator.setText(quotationLog.updatorCName);

    }


    private void createUIComponents() {



        JDatePanelImpl picker = new JDatePanelImpl(null);
        qDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());
          picker = new JDatePanelImpl(null);
        vDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());




        cb_customer = new JComboBox<Customer>();
        for (Customer customer : CacheManager.getInstance().bufferData.customers) {
            cb_customer.addItem(customer);
        }

        cb_salesman=  new JComboBox<User>();
        for (User salesman : CacheManager.getInstance().bufferData.salesmans) {
            cb_salesman.addItem(salesman);
        }

        //crate table  and addd table group header
        tb = new JHdTable(   ) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer,row,column);
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                }
                return comp;
            }
        };
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Window getWindow() {
        return getWindow(getRoot());
    }
}
