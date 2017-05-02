package com.giants.hd.desktop.dialogs;


import com.giants.hd.desktop.mvp.AbstractModel;
import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 */
public abstract class MVPDialog<T, K extends AbstractViewer,M extends AbstractModel> extends BaseDialog<T> {


    K viewer;
    M  model;

    public MVPDialog(Window window, String title) {
        super(window, title);

        viewer = createViewer();
        if (viewer != null)

            setContentPane(viewer.getRoot());

        model=createModel();



        setMinimumSize(new Dimension(400, 300));



    }


    protected abstract K createViewer();



    protected  abstract  M createModel();

    public  M  getModel()
    {
        return model;
    }

    protected K getViewer() {
        return viewer;
    }


}




