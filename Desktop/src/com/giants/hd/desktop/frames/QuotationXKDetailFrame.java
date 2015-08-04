package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_QuotationXKDetail;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
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
public class QuotationXKDetailFrame extends BaseFrame implements  BasePanel.PanelListener {



    @Inject
    ApiManager apiManager;
    @Inject
    Panel_QuotationXKDetail panel_QuotationXKDetail;

    private QuotationDetail oldData;
    private QuotationDetail quotationDetail;
    public QuotationXKDetailFrame(final Quotation quotation)
    {




        super("咸康报价详情[" + quotation.qNumber + "]");


        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadQuotationDetail(quotation);
            }
        });

    }
    public QuotationXKDetailFrame()
    {




        super("新增咸康报价单");

        init();
        QuotationDetail   newDetail=new QuotationDetail();
        newDetail.init();
        setQuotationDetail(newDetail);



    }





    public void init()
    {






        setContentPane(panel_QuotationXKDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {


                if (panel_QuotationXKDetail.data == null) {
                    dispose();
                    return;
                }

                panel_QuotationXKDetail.getData(quotationDetail);

                if (!quotationDetail.equals(oldData) ) {

                    int option = JOptionPane.showConfirmDialog(QuotationXKDetailFrame.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

                    if (JOptionPane.OK_OPTION == option) {
                        //点击了确定按钮

                        QuotationXKDetailFrame.this.dispose();
                    }

                } else {
                    //点击了确定按钮

                    QuotationXKDetailFrame.this.dispose();
                }


            }
        });



        panel_QuotationXKDetail.setListener(this);



    }



    private void setQuotationDetail(QuotationDetail newDetail)
    {


        oldData= (QuotationDetail) ObjectUtils.deepCopy(newDetail);
        this.quotationDetail=newDetail;
        panel_QuotationXKDetail.setData(newDetail);

    }

    @Override
    public void save() {


        try {
            panel_QuotationXKDetail.checkData(quotationDetail);

        } catch (HdUIException e)
        {
            JOptionPane.showMessageDialog(e.component,e.message);
            e.component.requestFocus();
            return;
        }


        panel_QuotationXKDetail.getData(quotationDetail);


        if(quotationDetail.quotation.quotationTypeId==0) {
            quotationDetail.quotation.quotationTypeId = 2;

            quotationDetail.quotation.quotationTypeName = "咸康报价";
        }
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

                    JOptionPane.showMessageDialog(QuotationXKDetailFrame.this,"删除成功！");

                    QuotationXKDetailFrame.this.dispose();



                }else
                {
                    JOptionPane.showMessageDialog(QuotationXKDetailFrame.this,data.message);
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

                    JOptionPane.showMessageDialog(QuotationXKDetailFrame.this,data.message);
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
                    JOptionPane.showMessageDialog(QuotationXKDetailFrame.this, "保存成功");

                }else {

                    JOptionPane.showMessageDialog(QuotationXKDetailFrame.this,data.message);


                }
            }
        }.go();


    }
}
