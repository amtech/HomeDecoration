package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.DefaultModel;
import com.giants.hd.desktop.view.AbstractViewer;

import java.awt.*;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public  abstract  class BaseMVPFrame<V  extends AbstractViewer> extends  MVPFrame< V, DefaultModel> {




    public BaseMVPFrame(String title) {
        super(title);
    }


    @Override
    public DefaultModel createModel() {
        return new DefaultModel();
    }


}
