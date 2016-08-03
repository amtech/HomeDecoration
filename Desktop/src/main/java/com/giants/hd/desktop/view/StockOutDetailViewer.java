package com.giants.hd.desktop.view;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;

import java.util.List;
import java.util.Set;

/**出库详情展示接口
 * Created by davidleen29 on 2016/7/18.
 */
public interface StockOutDetailViewer extends AbstractViewer {



    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail);




    /**
     * 设置柜号信息
     * @param guiInfos
     */
    void  showGuihaoData(Set<StockOutDetailFrame.GuiInfo> guiInfos);

    /**
     * 显示出库单项目
     * @param itemList
     */
    void showItems(List<ErpStockOutItem> itemList);

    /**
     * 设置是否编辑权限
     * @param b
     */
    void setEditable(boolean b);

    void setStockOutPriceVisible(boolean visible);
}
