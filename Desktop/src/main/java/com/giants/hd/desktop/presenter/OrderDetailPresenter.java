package com.giants.hd.desktop.presenter;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;

import java.io.File;

/**
 *
 * 订单详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface OrderDetailPresenter extends    Presenter {


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
     * 打印订单明细数据
     * @param orderItem
     */
    void printPaint(ErpOrderItem orderItem);

    void showProductDetail(ErpOrderItem orderItem);
}