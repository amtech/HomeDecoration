package com.giants.hd.desktop.frames;

import com.google.inject.Guice;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.imageio.ImageIO;
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

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("icons/logo.jpg")));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }









}
