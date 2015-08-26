package com.giants.hd.desktop.frames;

import com.google.inject.Guice;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.swing.*;
import java.awt.*;

/**
 *  所有frame 框架的基类
 */
public class BaseFrame  extends JFrame{




    public BaseFrame(  ) {

        this("");


    }

    public BaseFrame(  String title) {
        super(title);
        //setIcon;
        Guice.createInjector().injectMembers(this);
    }









}
