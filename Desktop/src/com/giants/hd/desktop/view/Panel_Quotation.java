package com.giants.hd.desktop.view;



import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.CacheManager;
import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants.hd.desktop.frames.QuotationDetailFrame;
import com.giants.hd.desktop.frames.QuotationXKDetailFrame;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.QuotationItemXKTableModel;
import com.giants.hd.desktop.model.QuotationTableModel;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.header.ColumnGroup;
import com.giants.hd.desktop.widget.header.GroupableTableHeader;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.User;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;

/**
 * 报价列表界面
 */
public class Panel_Quotation extends BasePanel{
    private JPanel panel1;
    private JTextField jtf_product;
    private JButton btn_search;
    private JButton btn_add;
    private JHdTable tb;
    private Panel_Page pagePanel;
    private JButton btn_add_xiankang;
    private JComboBox cb_salesman;


    @Inject
    ApiManager apiManager;

    @Inject
    QuotationTableModel tableModel;

    boolean limitSelf;
    public Panel_Quotation()
    {
        limitSelf= CacheManager.getInstance().bufferData.quoteAuth.limitSelf;

       tb.setModel(tableModel);


        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });


    }

    private void init() {



        cb_salesman.setVisible(!limitSelf);

        User all=new User();
        all.id=-1;
        all.code="--";
        all.name="--";
        all.chineseName="所有人";
        cb_salesman.addItem(all);
        for(User user:CacheManager.getInstance().bufferData.salesmans)
        {
            cb_salesman.addItem(user);
        }


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                loadData();
            }
        });

        jtf_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loadData();
            }
        });

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                loadData(pageIndex, pageSize);

            }
        });

        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    Quotation quotation = tableModel.getItem(tb.convertRowIndexToModel(tb.getSelectedRow()));
                    if (quotation.id <= 0)
                        return;
                    JFrame frame;
                    if (quotation.quotationTypeId == 2) {
                        frame = new QuotationXKDetailFrame(quotation);

                    } else {
                        frame = new QuotationDetailFrame(quotation);
                    }

                    frame.setLocationRelativeTo(getRoot());
                    frame.setVisible(true);
                }
            }
        });





        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JFrame frame = new QuotationDetailFrame();
                frame.setLocationRelativeTo(getRoot());
                frame.setVisible(true);

            }
        });



        btn_add_xiankang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JFrame frame = new QuotationXKDetailFrame();
                frame.setLocationRelativeTo(getRoot());
                frame.setVisible(true);

            }
        });




        boolean quotationAddable=AuthorityUtil.getInstance().addQuotation();

        btn_add.setVisible(quotationAddable);
        btn_add_xiankang.setVisible(quotationAddable);
    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }


    /**
     * 读取数据
     */
    public  void loadData()

    {

        loadData(0,pagePanel.getPageSize());

    }


    private void loadData(final int pageIndex,final int pageSize)
    {
        final  String searchValue=jtf_product.getText().trim();

      final  long salemansId;
        if(limitSelf)
        {
            salemansId=CacheManager.getInstance().bufferData.loginUser.id;
        }else
        {
            salemansId=((User)cb_salesman.getSelectedItem()).id;
        }


        new HdSwingWorker<Quotation,Object>( getWindow(getRoot()))
        {
            @Override
            protected RemoteData<Quotation> doInBackground() throws Exception {


                return   apiManager.loadQuotation(searchValue,salemansId,pageIndex,pageSize);

            }

            @Override
            public void onResult(RemoteData<Quotation> data) {


                pagePanel.bindRemoteData(data);
                tableModel.setDatas(data.datas);

            }
        }.go();



    }


}
