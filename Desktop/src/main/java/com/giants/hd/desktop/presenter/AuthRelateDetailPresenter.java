package com.giants.hd.desktop.presenter;

/**
 *
 *   权限明细详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface AuthRelateDetailPresenter extends    OrderAuthDetailPresenter,StockOutAuthDetailPresenter,QuoteAuthDetailPresenter {


    void setSelectedPane(int selectedIndex);

    void save();
}
