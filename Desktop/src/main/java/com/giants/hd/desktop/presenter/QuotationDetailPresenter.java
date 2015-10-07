package com.giants.hd.desktop.presenter;

/**
 * presenter layer of MVP
 */
public interface QuotationDetailPresenter extends  Presenter {


    public void save();


    void delete();

    void unVerify();

    void verify();
}
