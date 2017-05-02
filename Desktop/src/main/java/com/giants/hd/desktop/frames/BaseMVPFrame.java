package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public  abstract  class BaseMVPFrame<T extends AbstractViewer> extends  BaseInternalFrame {


    T viewer;

    public BaseMVPFrame(String title) {
        super(title);
    }

    @Override
    protected Container getCustomContentPane() {
        viewer=createViewer();
        if(viewer!=null)
            return   viewer.getRoot();
        return null;
    }

    protected  abstract  T createViewer();

    protected T getViewer()
    {

        return viewer;
    }
}
