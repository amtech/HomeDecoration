package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.interf.ComonSearch;
import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.logging.Logger;

/**
 * 报价单详情
 *
 * Created by davidleen29 on 2015/6/30.
 */
public class Panel_QuotationDetail extends BasePanel {
    private JHdTable tb;
    private JPanel root;
    private JTextArea textArea1;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JDatePickerImpl qDate;
    private JDatePickerImpl vDate;
    public Quotation quotation;


    @Inject
    ApiManager apiManager;


    @Inject
    QuotationItemTableModel model;

    public Panel_QuotationDetail() {


        init();
    }

    private void init() {

        tb.setModel(model);


        configTableEditor();


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

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public void setData(Quotation data) {
    }

    public void getData(Quotation data) {
    }

    public boolean isModified(Quotation data) {
        return false;
    }

    private void createUIComponents() {



        JDatePanelImpl picker = new JDatePanelImpl(null);
        qDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());
          picker = new JDatePanelImpl(null);
        vDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());



    }
}
