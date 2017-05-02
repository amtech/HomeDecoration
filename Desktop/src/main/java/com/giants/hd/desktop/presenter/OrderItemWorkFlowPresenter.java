package com.giants.hd.desktop.presenter;

/**
 *
 *
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderItemWorkFlowPresenter extends  Presenter {


    void save();



    void reimportProduct();

    /**
     * 全部撤销排厂
     */
    void cancelArrange();
}
