package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_Quotation;
import com.giants.hd.desktop.view.Panel_QuotationDetail;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.Quotation;
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
  public QuotationDetailFrame(Quotation quotation)
    {




        super("报价详情[" + quotation.qNumber + "]");


        init();

        panel_QuotationDetail.setQuotation(quotation);
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



                    if (panel_QuotationDetail.quotation == null) {
                        dispose();
                        return;
                    }

                    if (panel_QuotationDetail.isModified(null)) {

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




    }



    @Override
    public void save() {


    }

    @Override
    public void delete() {




       // final Quotation detail=panel_QuotationDetail.getData();

//        if(detail.product.id<=0)
//        {
//
//            JOptionPane.showMessageDialog(this, "产品数据未建立，请先保存");
//            return;
//
//        }



     int res=   JOptionPane.showConfirmDialog(this, "是否删除报价单？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.YES_OPTION)
        {
        new HdSwingWorker<Void,Void>(this)
        {

            @Override
            protected RemoteData<Void> doInBackground() throws Exception {

               // return     apiManager.deleteProductLogic(detail.product.id);
                return null;

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




//    /**
//     * 加载产品详情信息
//     */
//    private void loadQuoationDetail(final Product product) {
//
//
//        new HdSwingWorker<Quotation, Long>(this) {
//            @Override
//            protected RemoteData<Quotation> doInBackground() throws Exception {
//                return apiManager.loadQuotation(product.id);
//            }
//
//            @Override
//            public void onResult(RemoteData<Quotation> data) {
//
//                if(data.isSuccess()) {
//
//                    Quotation detail = data.datas.get(0);
//
//                    panel_Quotation.setQuotation(detail);
//                }else
//                {
//
//                    JOptionPane.showMessageDialog(QuotationDetailFrame.this,data.message);
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            close();
//                        }
//                    });
//
//                }
//            }
//        }.go();
//
//
//    }
}
