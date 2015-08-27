package com.giants.hd.desktop.frames;

import com.google.inject.Guice;

import javax.imageio.ImageIO;
import javax.swing.*;

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
