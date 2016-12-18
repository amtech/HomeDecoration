package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.StockXiaoku;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface StockXiaokuPresenter<T>  extends  Presenter {


    public void search(String key,int pageIndex, int pageSize) ;

    void onListItemClick(StockXiaoku xiaoku);
}
