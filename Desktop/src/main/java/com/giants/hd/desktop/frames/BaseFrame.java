package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.Presenter;
import com.google.inject.Guice;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *  所有frame 框架的基类
 */
public class BaseFrame  extends JFrame  implements Presenter {




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


    @Override
    public void close() {
        dispose();
    }
}
