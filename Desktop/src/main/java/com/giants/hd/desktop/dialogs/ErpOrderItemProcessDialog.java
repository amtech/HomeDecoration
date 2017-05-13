package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.ErpOrderItemProcessMVP;
import com.giants3.hd.utils.entity.ErpOrderItemProcess;

import java.awt.*;

/**
 * 订单排产明细列表
 */
public class ErpOrderItemProcessDialog extends MVPDialog<ErpOrderItemProcess, ErpOrderItemProcessMVP.AViewer, ErpOrderItemProcessMVP.AModel> implements ErpOrderItemProcessMVP.APresenter {


    public ErpOrderItemProcessDialog(Window window, String osNo, String prdNo) {
        super(window, "订单款项排厂明细");

        setMinimumSize(new Dimension(800, 600));


    }


    @Override
    protected ErpOrderItemProcessMVP.AViewer createViewer() {
        return null;
    }

    @Override
    protected ErpOrderItemProcessMVP.AModel createModel() {
        return null;
    }
}
