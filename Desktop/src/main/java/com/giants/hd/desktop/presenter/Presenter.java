package com.giants.hd.desktop.presenter;


/**
 * Interface representing a Presenter in a model viewImpl presenter (MVP) pattern.
 */
public interface Presenter {


//    void create();
    void close();

    /**
     * 判断数据是否修改接口
     * @return
     */
    boolean  hasModifyData();
}
