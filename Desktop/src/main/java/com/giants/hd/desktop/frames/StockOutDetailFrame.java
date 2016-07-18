package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.StockOutDetailPresenter;
import com.giants.hd.desktop.view.StockOutDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_StockOutDetail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 出库单详情模块
 */
public class StockOutDetailFrame extends BaseFrame implements StockOutDetailPresenter {


    @Inject
    ApiManager apiManager;


    StockOutDetailViewer panel_stockOutDetail;


    private ErpStockOutDetail oldData;
    private ErpStockOutDetail erpStockOutDetail;

    public StockOutDetailFrame(final ErpStockOut stockOut) {

        super();
        panel_stockOutDetail = new Panel_StockOutDetail(this);
        init();
        readData(stockOut);
    }

    private void readData(ErpStockOut erpStockOut) {

        UseCaseFactory.getInstance().createStockOutDetailUseCase(erpStockOut.ck_no).execute(new Subscriber<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void onCompleted() {
                panel_stockOutDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panel_stockOutDetail.hideLoadingDialog();
                panel_stockOutDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpStockOutDetail> erpOrderRemoteData) {
                if (erpOrderRemoteData.isSuccess()) {
                    panel_stockOutDetail.setStockOutDetail(erpOrderRemoteData.datas.get(0));
                }


            }
        });

    }


    public void init() {
        setContentPane(panel_stockOutDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {


                if (panel_stockOutDetail == null || erpStockOutDetail == null) {
                    dispose();
                    return;
                }

//                panel_QuotationDetail.getData(quotationDetail);

                if (!erpStockOutDetail.equals(oldData)) {

                    int option = JOptionPane.showConfirmDialog(StockOutDetailFrame.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

                    if (JOptionPane.OK_OPTION == option) {
                        //点击了确定按钮

                        StockOutDetailFrame.this.dispose();
                    }

                } else {
                    //点击了确定按钮

                    StockOutDetailFrame.this.dispose();
                }


            }
        });


    }


    private void setErpStockOutDetail(ErpStockOutDetail newDetail) {

        oldData = (ErpStockOutDetail) ObjectUtils.deepCopy(newDetail);

        panel_stockOutDetail.setStockOutDetail(newDetail);

    }


    /**
     * 保存详情信息
     */
    private void saveQuotationDetail(final QuotationDetail quotationDetail) {


//        new HdSwingWorker<ErpStockOutDetail, Long>(this) {
//            @Override
//            protected RemoteData<ErpStockOutDetail> doInBackground() throws Exception {
//                return apiManager.saveSt(quotationDetail);
//            }
//
//            @Override
//            public void onResult(RemoteData<ErpStockOutDetail> data) {
//
//                if(data.isSuccess()) {
//
//                    setErpStockOutDetail(data.datas.get(0));
//                    JOptionPane.showMessageDialog(StockOutDetailFrame.this, "保存成功");
//
//                }else {
//
//                    JOptionPane.showMessageDialog(StockOutDetailFrame.this,data.message);
//
//
//                }
//            }
//        }.go();


    }

    /**
     * 保存详情信息
     */
    private void saveAndVerifyQuotationDetail(final QuotationDetail quotationDetail) {


//        new HdSwingWorker<St, Long>(this) {
//            @Override
//            protected RemoteData<QuotationDetail> doInBackground() throws Exception {
//                return apiManager.saveAndVerifyQuotationDetail(quotationDetail);
//            }
//
//            @Override
//            public void onResult(RemoteData<QuotationDetail> data) {
//
//                if(data.isSuccess()) {
//
//                    setErpStockOutDetail(data.datas.get(0));
//                    JOptionPane.showMessageDialog(StockOutDetailFrame.this, "保存/审核成功");
//
//                }else {
//
//                    JOptionPane.showMessageDialog(StockOutDetailFrame.this,data.message);
//
//
//                }
//            }
//        }.go();


    }

    @Override
    public void save() {

//
//        try {
//            panel_QuotationDetail.checkData(quotationDetail);
//        } catch (HdUIException e)
//        {
//            JOptionPane.showMessageDialog(e.component,e.message);
//            e.component.requestFocus();
//            return;
//        }
//
//
//        panel_QuotationDetail.getData(quotationDetail);
//
//
//
//
//        if (quotationDetail.equals(oldData)) {
//            JOptionPane.showMessageDialog(StockOutDetailFrame.this, "数据无改动");
//            return;
//        }
//
//
//        saveQuotationDetail(quotationDetail);


    }


    @Override
    public void delete() {


//        final QuotationDetail detail= quotationDetail;
//        if(detail==null)return;
//
//        if(detail.quotation.id<=0)
//        {
//
//            JOptionPane.showMessageDialog(StockOutDetailFrame.this, "产品数据未建立，请先保存");
//            return;
//
//        }
//
//
//
//        int res=   JOptionPane.showConfirmDialog(StockOutDetailFrame.this, "是否删除报价单？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
//        if(res==JOptionPane.YES_OPTION)
//        {
//            new HdSwingWorker<Void,Void>(StockOutDetailFrame.this)
//            {
//
//                @Override
//                protected RemoteData<Void> doInBackground() throws Exception {
//
//                    return     apiManager.deleteQuotationLogic(detail.quotation.id);
//
//
//                }
//
//                @Override
//                public void onResult(RemoteData<Void> data) {
//
//                    if(data.isSuccess())
//                    {
//
//                        JOptionPane.showMessageDialog(StockOutDetailFrame.this,"删除成功！");
//
//                        StockOutDetailFrame.this.dispose();
//
//
//
//                    }else
//                    {
//                        JOptionPane.showMessageDialog(StockOutDetailFrame.this,data.message);
//                    }
//
//                }
//            }.go();
//
//
//
//        }


    }

    @Override
    public void verify() {

    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }


}
