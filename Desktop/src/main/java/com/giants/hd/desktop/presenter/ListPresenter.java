package com.giants.hd.desktop.presenter;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface ListPresenter<T> extends  Presenter {

    public void search(String key,long salesId, int pageIndex, int pageSize) ;



    public void onListItemClick(T data);
    }
