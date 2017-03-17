package com.giants.hd.desktop.presenter;

/**
 *
 */
public interface ZhilingdanPresenter extends    Presenter {


    void search(String key, String startDateString, String endDateString);

    void showAll(boolean allSelected, boolean  caigouSelected, boolean jinhuoSelected);
}
