package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.StockOutDetailPresenter;
import com.giants.hd.desktop.view.StockOutDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_StockOutDetail;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.*;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 出库单详情模块
 */
public class StockOutDetailFrame extends BaseFrame implements StockOutDetailPresenter {


    StockOutDetailViewer stockOutDetailViewer;
    java.util.List<String> attachStrings = new ArrayList<>();

    //柜号数组信息
    Set<GuiInfo> guiInfos = new HashSet<>();
    private ErpStockOutDetail oldData;
    private ErpStockOutDetail erpStockOutDetail;

    public StockOutDetailFrame(final ErpStockOut stockOut) {

        super("出库单：" + stockOut.ck_no);
        stockOutDetailViewer = new Panel_StockOutDetail(this);
        init();
        readData(stockOut);
    }

    private void readData(ErpStockOut erpStockOut) {

        UseCaseFactory.getInstance().createStockOutDetailUseCase(erpStockOut.ck_no).execute(new Subscriber<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void onCompleted() {
                stockOutDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                stockOutDetailViewer.hideLoadingDialog();
                stockOutDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpStockOutDetail> erpOrderRemoteData) {
                if (erpOrderRemoteData.isSuccess()) {
                    setErpStockOutDetail(erpOrderRemoteData.datas.get(0));
                }


            }
        });

    }


    public void init() {
        setContentPane(stockOutDetailViewer.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {


                if (stockOutDetailViewer == null || erpStockOutDetail == null) {
                    dispose();
                    return;
                }

//                panel_QuotationDetail.getData(quotationDetail);
                erpStockOutDetail.erpStockOut.attaches = StringUtils.combine(attachStrings);
                if (!GsonUtils.toJson(erpStockOutDetail).equals(GsonUtils.toJson(oldData))) {

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
        erpStockOutDetail = newDetail;

        stockOutDetailViewer.setStockOutDetail(newDetail);
        attachStrings = StringUtils.isEmpty(erpStockOutDetail.erpStockOut.attaches) ? new ArrayList<String>() : (ArrayUtils.changeArrayToList(erpStockOutDetail.erpStockOut.attaches.split(";")));
        stockOutDetailViewer.showAttachFiles(attachStrings);


        guiInfos.clear();
        //整理出柜号信息数据
        for (ErpStockOutItem item : erpStockOutDetail.items) {
            if (StringUtils.isEmpty(item.guihao) && StringUtils.isEmpty(item.fengqianhao))
                continue;
            guiInfos.add(new GuiInfo(item.guihao, item.fengqianhao));
        }

        stockOutDetailViewer.showGuihaoData(guiInfos);


    }


    @Override
    public void save() {


        erpStockOutDetail.erpStockOut.attaches = StringUtils.combine(attachStrings);
        UseCaseFactory.getInstance().saveStockOutDetail(erpStockOutDetail).execute(new Subscriber<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void onCompleted() {
                stockOutDetailViewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {

                stockOutDetailViewer.hideLoadingDialog();
                stockOutDetailViewer.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<ErpStockOutDetail> remoteData) {
                stockOutDetailViewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    setErpStockOutDetail(remoteData.datas.get(0));

                    stockOutDetailViewer.showMesssage("保存成功!");
                } else {

                    stockOutDetailViewer.showMesssage("保存失败!" + remoteData.message);
                }


            }
        });

        stockOutDetailViewer.showLoadingDialog("正在保存。。。");
    }


    @Override
    public void delete() {


    }

    @Override
    public void verify() {

    }

    @Override
    public void addPictureClick(File[] file) {


        //上传图片
        UseCaseFactory.getInstance().uploadTempFileUseCase(file).execute(new Subscriber<java.util.List<String>>() {
            @Override
            public void onCompleted() {
                stockOutDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                stockOutDetailViewer.hideLoadingDialog();
                stockOutDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<String> stringList) {
                attachStrings.addAll(stringList);
                stockOutDetailViewer.showAttachFiles(attachStrings);
            }
        });
        stockOutDetailViewer.showLoadingDialog("正在上传图片。。。");


    }

    @Override
    public void onCemaiChange(String value) {

        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;
        erpStockOutDetail.erpStockOut.cemai = value;
    }

    @Override
    public void onNeihemaiChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.neheimai = value;

    }

    @Override
    public void onZhengmaiChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.zhengmai = value;

    }

    @Override
    public void onMemoChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.memo = value;


    }

    @Override
    public void onDeleteAttach(String url) {
        attachStrings.remove(url);
    }


    @Override
    public void addGuiInfo(String guihao, String fengqian) {

        GuiInfo guiInfo = new GuiInfo(guihao, fengqian);
        guiInfos.add(guiInfo);
        stockOutDetailViewer.showGuihaoData(guiInfos);

    }

    @Override
    public void removeGuiInfo(GuiInfo guiInfo) {
        guiInfos.remove(guiInfo);
        stockOutDetailViewer.showGuihaoData(guiInfos);
    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }


    /**
     * 柜号相关信息数据
     */

    public static class GuiInfo {

        public String guihao;

        public String fengqianhao;

        public GuiInfo() {
        }

        public GuiInfo(String guihao, String fengqianhao) {

            this.guihao = guihao;
            this.fengqianhao = fengqianhao;
        }


        @Override
        public String toString() {
            return guihao + "        " + fengqianhao;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GuiInfo)) return false;

            GuiInfo guiInfo = (GuiInfo) o;

            if (guihao != null ? !guihao.equals(guiInfo.guihao) : guiInfo.guihao != null) return false;
            return fengqianhao != null ? fengqianhao.equals(guiInfo.fengqianhao) : guiInfo.fengqianhao == null;

        }

        @Override
        public int hashCode() {
            int result = guihao != null ? guihao.hashCode() : 0;
            result = 31 * result + (fengqianhao != null ? fengqianhao.hashCode() : 0);
            return result;
        }
    }

}
