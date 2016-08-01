package com.giants.hd.desktop.view;

/**
 *
 *   权限明细类界面展示曾
 * Created by davidleen29 on 2016/8/1.
 */
public interface AuthRelateDetailViewer extends  OrderAuthDetailViewer,StockOutAuthDetailViewer,QuoteAuthDetailViewer {
    void showPaneAndRow(int selectedPane, int showRow);
}
