package com.giants.hd.desktop.presenter;

import java.util.List;

/**
 *
 *    出库权限明细详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface StockOutAuthDetailPresenter extends    Presenter {

    void onRelateUsesSelected(List<Integer> indexes);

}
