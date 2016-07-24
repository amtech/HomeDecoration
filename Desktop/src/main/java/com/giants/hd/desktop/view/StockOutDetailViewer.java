package com.giants.hd.desktop.view;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;

import java.util.List;
import java.util.Set;

/**出库详情展示接口
 * Created by davidleen29 on 2016/7/18.
 */
public interface StockOutDetailViewer extends AbstractViewer {



    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail);


    /**
     * 显示附件
     * @param attachStrings
     */
    void showAttachFiles(List<String> attachStrings);

    /**
     * 设置柜号信息
     * @param guiInfos
     */
    void  showGuihaoData(Set<StockOutDetailFrame.GuiInfo> guiInfos);
}
