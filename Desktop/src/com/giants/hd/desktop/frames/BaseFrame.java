package com.giants.hd.desktop.frames;

import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;

/**
 *  所有frame 框架的基类
 */
public class BaseFrame  extends JFrame{




    public BaseFrame(  ) {

        this(" ");

    }

    public BaseFrame(  String title) {
        super(title);

        Guice.createInjector().injectMembers(this);
    }









}
