package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.AbstractModel;
import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * mvp 结构frame
 * Created by davidleen29 on 2017/4/7.
 */
public  abstract  class MVPFrame<V extends AbstractViewer,M extends AbstractModel> extends  BaseInternalFrame {


    V viewer;
    M model;

    public MVPFrame(String title) {
        super(title);
        model=createModel();

    }

    @Override
    protected Container getCustomContentPane() {
        viewer=createViewer();
        if(viewer!=null)
            return   viewer.getRoot();
        return null;
    }

    protected  abstract  V createViewer();

    protected V getViewer()
    {

        return viewer;
    }
    protected  abstract M createModel();


    protected M getModel()
    {
        return model;
    }

}
