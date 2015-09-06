package com.giants.hd.desktop.view;

import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;

/**
 * 基础模本类。  提供guice注射等等功能  提供一些公共方法
 */
public  abstract  class BasePanel {

    protected PanelListener listener;


    public PanelListener getListener() {
        return listener;
    }

    public void setListener(PanelListener listener) {
        this.listener = listener;
    }

    public BasePanel()
    {

        Guice.createInjector().injectMembers(this);
    }


    /**
     * 遍历返回任意控件的frame
     * @param component
     * @return
     */
    protected Window getWindow(Component component)
    {

        while(component!=null&&!(component instanceof Window))
            component=component.getParent();
        return (Window)component;
    }


    /**
     * 获取实际控件
     * @return
     */
    public abstract JComponent getRoot();


    /**
     * 面板回调接口
     */
    public interface  PanelListener
    {

        public void save();

        public void delete();


       public  void close();

        public void verify();

        public void unVerify();
    }



    public static class PanelAdapter implements PanelListener
    {
        @Override
        public void save() {

        }

        @Override
        public void delete() {

        }

        @Override
        public void close() {

        }

        @Override
        public void verify() {

        }

        @Override
        public void unVerify() {

        }
    }



}
