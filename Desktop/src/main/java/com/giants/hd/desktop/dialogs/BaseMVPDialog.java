package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.AbstractModel;
import com.giants.hd.desktop.mvp.DefaultModel;
import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 */
public abstract class BaseMVPDialog<T, K extends AbstractViewer> extends MVPDialog<T, K, DefaultModel> {


    public BaseMVPDialog(Window window, String title) {
        super(window, title);

    }



    @Override
    public DefaultModel createModel() {
        return new DefaultModel();
    }


}




