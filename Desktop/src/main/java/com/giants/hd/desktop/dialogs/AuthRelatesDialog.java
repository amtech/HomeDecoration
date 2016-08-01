package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.QuoteAuthModel;
import com.giants.hd.desktop.presenter.AuthRelateDetailPresenter;
import com.giants.hd.desktop.view.AuthRelateDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_Auth_Relates;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * n 细分权限业务处理层
 */
public class AuthRelatesDialog extends BaseDialog implements AuthRelateDetailPresenter {


    @Inject
    ApiManager apiManager;

    List<User> sales;
    int showRow;
    private AuthRelateDetailViewer viewer;


    //当前显示的面板  0  报价 1 订单  2 出库
    private int selectedPane = 0;

    private List<QuoteAuth> quoteAuths;

//    private List<StockAuth>


    public AuthRelatesDialog(Window window) {
        super(window);
        setTitle("报价细分权限");
        viewer = new Panel_Auth_Relates(this, this);
        setContentPane(viewer.getRoot());
        sales = CacheManager.getInstance().bufferData.salesmans;
        viewer.showAllSales(sales);

        loadQuoteAuth();
    }

    private void loadQuoteAuth() {

        UseCaseFactory.getInstance().createQuoteAuthListCase().execute(new Subscriber<RemoteData<QuoteAuth>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<QuoteAuth> erpOrderRemoteData) {
                if (erpOrderRemoteData.isSuccess()) {
                    setQuoteAuthData(erpOrderRemoteData.datas);
                }


            }


        });
        viewer.showLoadingDialog();
    }


    private void setQuoteAuthData(List<QuoteAuth> quoteAuth) {
        quoteAuths = quoteAuth;
        viewer.showQuoteAuthList(quoteAuth);
    }


    /**
     * 显示关联业务员状态
     *
     * @param relatedSales
     */
    private void showSaleRelate(String relatedSales) {


        String[] ids = StringUtils.isEmpty(relatedSales) ? null : relatedSales.split(",|,");


        List<Integer> indexes = new ArrayList<>();
        int size = sales.size();
        if (ids
                != null)
            for (int i = 0; i < size; i++) {

                for (String id : ids) {

                    if (String.valueOf(sales.get(i).id).equals(id)) {
                        indexes.add(i);
                        break;
                    }
                }

            }
        viewer.bindRelateSalesData(indexes);
    }




    @Override
    public void onQuoteAuthRowSelected(int row) {
        showRow=row;
        showSaleRelate(quoteAuths.get(row).relatedSales);
    }

    @Override
    public void setSelectedPane(int selectedIndex) {


        selectedPane = selectedIndex;

        showRow = 0;
        switch (selectedPane) {
            case 0:
                viewer.showPaneAndRow(selectedPane, showRow);
                break;
            case 1:
                break;
            case 2:
                break;
        }


    }

    @Override
    public void onRelateUsesSelected(List<Integer> indexes) {


        StringBuilder sb = new StringBuilder();
        for (int i : indexes) {

            sb.append(sales.get(i).id).append(",");


        }


        switch (selectedPane) {
            case 0:
                quoteAuths.get(showRow).relatedSales = sb.toString();
                break;
            case 1:
                break;
            case 2:
                break;
        }


    }
}
