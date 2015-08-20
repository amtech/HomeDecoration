package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.CacheManager;
import com.giants.hd.desktop.dialogs.ExportQuotationDialog;
import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.interf.ComonSearch;

import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;

/**
 * 报价单详情
 *
 * Created by davidleen29 on 2015/6/30.
 */
public class Panel_QuotationDetail extends BasePanel {
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
    public QuotationDetail data;


    @Inject
    ApiManager apiManager;



    QuotationItemTableModel model;




    public Panel_QuotationDetail() {


        init();
    }

    private void init() {
        model=new QuotationItemTableModel();
        tb.setModel(model);


        configTableEditor();



        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (listener != null)
                    listener.save();
            }
        });


        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if (e.getClickCount() == 2) {


                    int column = tb.convertColumnIndexToModel(tb.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 1) {

                        QuotationItem item = model.getItem(tb.convertRowIndexToModel(tb.getSelectedRow()));
                        if (item.productId > 0) {
                            ImageViewDialog.showProductDialog(getWindow(getRoot()), item.productName, item.pVersion);
                        }

                    }

                }

            }
        });



        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (listener != null) {
                    listener.delete();
                }
            }
        });




        btn_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


               new  ExportQuotationDialog(SwingUtilities.getWindowAncestor(getRoot()),data).setVisible(true);



            }
        });



        //配置权限  是否修改  是否可以删除

        boolean modifiable= AuthorityUtil.getInstance().editQuotation()||AuthorityUtil.getInstance().addQuotation();

        btn_save.setVisible(modifiable);




        btn_delete.setVisible(AuthorityUtil.getInstance().deleteQuotation());


        btn_export.setVisible(AuthorityUtil.getInstance().exportQuotation());



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

                    productable .setProduct(data.datas.get(0), rowIndex);


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

    @Override
    public JComponent getRoot() {
        return root;
    }


    public void setData(QuotationDetail data) {


        this.data=data;
        Quotation quotation=data.quotation;

        jtf_number.setText(quotation.qNumber);
        qDate.getJFormattedTextField().setText(quotation.qDate);
        vDate.getJFormattedTextField().setText(quotation.vDate);

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
        data.items.clear();
        data.items.addAll(model.getValuableDatas());


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

    }
}
