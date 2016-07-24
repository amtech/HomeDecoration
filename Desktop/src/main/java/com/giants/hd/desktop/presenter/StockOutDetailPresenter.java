package com.giants.hd.desktop.presenter;

import com.giants.hd.desktop.frames.StockOutDetailFrame;

import java.io.File;

/**
 *
 * 出库详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface StockOutDetailPresenter extends    Presenter {


      void save();


    public void delete();


    public void verify();

    void addPictureClick(File[] file);

    /**
     * 侧唛数据改变
     * @param value
     */
    void onCemaiChange(String value);
    /**
     * 内盒数据改变
     * @param value
     */
    void onNeihemaiChange(String value);

    /**
     * 正唛数据改变
     * @param value
     */
    void onZhengmaiChange(String value);

    /**
     * 备注数据改变
     * @param value
     */
    void onMemoChange(String value);

    /**
     * 删除附件
     * @param url
     */
    void onDeleteAttach(String url);


    /**
     * 添加柜号数据
     * @param guihao
     * @param fengqian
     */
    void addGuiInfo(String guihao, String fengqian);

    /**
     * 移除柜号数据
     * @param guiInfo
     */
    void removeGuiInfo(StockOutDetailFrame.GuiInfo guiInfo);
}
