package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.StockOutDetailPresenter;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.view.StockOutDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_StockOutDetail;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import rx.Subscriber;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 出库单详情模块
 */
public class StockOutDetailFrame extends BaseFrame implements StockOutDetailPresenter {


    StockOutDetailViewer stockOutDetailViewer;

    //柜号数组信息
    Set<GuiInfo> guiInfos = new HashSet<>();
    private String oldData;
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




        //设置权限相关
      stockOutDetailViewer.setEditable(  AuthorityUtil.getInstance().editStockOut());

        stockOutDetailViewer.setStockOutPriceVisible(CacheManager.getInstance().isStockOutPriceVisible());
    }

    @Override
    public boolean hasModifyData() {
         return !GsonUtils.toJson(erpStockOutDetail).equals( oldData );
    }

    private void setErpStockOutDetail(ErpStockOutDetail newDetail) {

        oldData =GsonUtils.toJson(newDetail);
        erpStockOutDetail = newDetail;

        stockOutDetailViewer.setStockOutDetail(newDetail);

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
    public void filterGuihao(GuiInfo guiInfo) {


        java.util.List<ErpStockOutItem> itemList=new ArrayList<>();
        if(guiInfo==null||(StringUtils.isEmpty(guiInfo.guihao) ))
        {
            itemList.addAll(erpStockOutDetail.items);
        }else {
            for (ErpStockOutItem item : erpStockOutDetail.items) {
                if (guiInfo.guihao.equals(item.guihao) ) {
                    itemList.add(item);
                }
            }
        }




        stockOutDetailViewer.showItems(itemList);

    }


    @Override
    public void showOrderDetail(String os_no) {
        OrderDetailFrame orderDetailFrame=new OrderDetailFrame(os_no);
        orderDetailFrame.setLocationRelativeTo(this);
        orderDetailFrame.setVisible(true);
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
