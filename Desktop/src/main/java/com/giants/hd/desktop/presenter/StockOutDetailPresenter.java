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

    /**
     * 出库单项目  根据柜号过滤
     * @param guiInfo
     */
    void filterGuihao(StockOutDetailFrame.GuiInfo guiInfo);

    /**
     * 显示订单详情
     * @param os_no
     */
    void showOrderDetail(String os_no);

    /**
     * 导出发票单
     */
    void exportInvoice();


    /**
     * 导出出库清单
     */
    void  exportPack();
}
