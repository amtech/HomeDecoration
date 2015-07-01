package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_Quotation;
import com.giants.hd.desktop.view.Panel_QuotationDetail;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  报价单详情模块
 */
public class QuotationDetailFrame extends BaseFrame implements  BasePanel.PanelListener {



    @Inject
    ApiManager apiManager;
    @Inject
    Panel_QuotationDetail panel_QuotationDetail;

    private QuotationDetail oldData;
    private QuotationDetail quotationDetail;
    public QuotationDetailFrame(final Quotation quotation)
    {




        super("报价详情[" + quotation.qNumber + "]");


        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadQuotationDetail(quotation);
            }
        });

    }
    public QuotationDetailFrame( )
    {




        super("新增报价单");

        init();
     QuotationDetail   newDetail=new QuotationDetail();
        newDetail.init();
        setQuotationDetail(newDetail);



    }





    public void init()
    {






        setContentPane(panel_QuotationDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {


                if (panel_QuotationDetail.data == null) {
                    dispose();
                    return;
                }

                panel_QuotationDetail.getData(quotationDetail);

                if (!quotationDetail.equals(oldData) ) {

                    int option = JOptionPane.showConfirmDialog(QuotationDetailFrame.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

                    if (JOptionPane.OK_OPTION == option) {
                        //点击了确定按钮

                        QuotationDetailFrame.this.dispose();
                    }

                } else {
                    //点击了确定按钮

                    QuotationDetailFrame.this.dispose();
                }


            }
        });



        panel_QuotationDetail.setListener(this);



    }



    private void setQuotationDetail(QuotationDetail newDetail)
    {


        oldData= (QuotationDetail) ObjectUtils.deepCopy(newDetail);
        this.quotationDetail=newDetail;
        panel_QuotationDetail.setData(newDetail);

    }

    @Override
    public void save() {


        try {
            panel_QuotationDetail.checkData(quotationDetail);
        } catch (HdUIException e)
        {
            JOptionPane.showMessageDialog(e.component,e.message);
            e.component.requestFocus();
            return;
        }


        panel_QuotationDetail.getData(quotationDetail);

        saveQuotationDetail(quotationDetail);






    }

    @Override
    public void delete() {




        final QuotationDetail detail= quotationDetail;
        if(detail==null)return;

        if(detail.quotation.id<=0)
        {

            JOptionPane.showMessageDialog(this, "产品数据未建立，请先保存");
            return;

        }



     int res=   JOptionPane.showConfirmDialog(this, "是否删除报价单？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.YES_OPTION)
        {
        new HdSwingWorker<Void,Void>(this)
        {

            @Override
            protected RemoteData<Void> doInBackground() throws Exception {

               return     apiManager.deleteQuotationLogic(detail.quotation.id);


            }

            @Override
            public void onResult(RemoteData<Void> data) {

                if(data.isSuccess())
                {

                    JOptionPane.showMessageDialog(QuotationDetailFrame.this,"删除成功！");

                    QuotationDetailFrame.this.dispose();



                }else
                {
                    JOptionPane.showMessageDialog(QuotationDetailFrame.this,data.message);
                }

            }
        }.go();



        }


    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }




    /**
     * 加载产品详情信息
     */
    private void loadQuotationDetail(final Quotation quotation) {


        new HdSwingWorker<QuotationDetail, Long>(this) {
            @Override
            protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                return apiManager.loadQuotationDetail( quotation.id);
            }

            @Override
            public void onResult(RemoteData<QuotationDetail> data) {

                if(data.isSuccess()) {

                      setQuotationDetail(data.datas.get(0));

                }else {

                    JOptionPane.showMessageDialog(QuotationDetailFrame.this,data.message);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            close();
                        }
                    });

                }
            }
        }.go();


    }





    /**
     * 加载产品详情信息
     */
    private void saveQuotationDetail(final QuotationDetail quotationDetail) {


        new HdSwingWorker<QuotationDetail, Long>(this) {
            @Override
            protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                return apiManager.saveQuotationDetail(quotationDetail);
            }

            @Override
            public void onResult(RemoteData<QuotationDetail> data) {

                if(data.isSuccess()) {

                    setQuotationDetail(data.datas.get(0));
                    JOptionPane.showMessageDialog(QuotationDetailFrame.this, "保存成功");

                }else {

                    JOptionPane.showMessageDialog(QuotationDetailFrame.this,data.message);


                }
            }
        }.go();


    }
}
