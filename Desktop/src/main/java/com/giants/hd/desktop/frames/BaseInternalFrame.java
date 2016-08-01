package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.Presenter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public abstract class BaseInternalFrame extends JInternalFrame  implements Presenter{


    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;
    static {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH=screenSize.width;
        SCREEN_HEIGHT=screenSize.height;


    }

    public BaseInternalFrame(String title) {
        super(title ,
                false, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        ++openFrameCount;

        //...Create the GUI and put it in the window...

        //...Then set the window size or call pack...
        setContentPane(getCustomContentPane());
        setSize(SCREEN_WIDTH/2,SCREEN_HEIGHT/2);


        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }



    protected abstract Container getCustomContentPane();


    @Override
    public void close() {

    }

    @Override
    public boolean hasModifyData() {
        return false;
    }
}
