package com.giants.hd.desktop.presenter;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface StockOutSubmitPresenter<T>  extends  Presenter {


    public void search(String key, String startDate, String endDate) ;

    /**
     * 格式1 导出
     */
    void exportExcel();


}
