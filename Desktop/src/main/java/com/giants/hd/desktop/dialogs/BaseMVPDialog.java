package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.AbstractModel;
import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 */
public abstract class BaseMVPDialog<T, K extends AbstractViewer> extends MVPDialog<T, K, BaseMVPDialog.DefaultModel> {


    public BaseMVPDialog(Window window, String title) {
        super(window, title);

    }



    @Override
    public BaseMVPDialog.DefaultModel createModel() {
        return new DefaultModel();
    }


    //a default model donoting;
    public class DefaultModel implements AbstractModel {
    }
}




