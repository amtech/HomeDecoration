package com.giants.hd.desktop.viewImpl;

import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 基础模本类。  提供guice注射等等功能  提供一些公共方法
 */
public  abstract  class BasePanel {

    protected PanelListener listener;
    private Window window;


    public PanelListener getListener() {
        return listener;
    }

    public void setListener(PanelListener listener) {
        this.listener = listener;
    }

    //加载进度条
    LoadingDialog dialog;

    public BasePanel()
    {

        Guice.createInjector().injectMembers(this);
    }

    public BasePanel(Window window)
    {

        this.window = window;
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
     * 遍历返回任意控件的frame
     * @param
     * @return
     */
    protected Window getWindow(  )
    {
        Component component=getRoot();
        if(component==null)
        {
            throw new RuntimeException(getClass().getCanonicalName()+" did not implements getRoot method and return a component");
        }
        while(component!=null&&!(component instanceof Window))
            component=component.getParent();
        return (Window)component;
    }


    /**
     * 获取实际控件
     * @return
     */
    public abstract JComponent getRoot();

    public void hideLoadingDialog() {

        if(dialog!=null)
        {
            dialog.setVisible(false);
            dialog.dispose();
            dialog=null;
        }
    }

    public void showMesssage(String message) {

        JOptionPane.showMessageDialog(getWindow(getRoot()),message);
    }



    public boolean showConfirmMessage(String message) {

        return JOptionPane.showConfirmDialog(getWindow(),message)== JOptionPane.YES_OPTION;
    }

    public void showLoadingDialog() {


        if(dialog==null)
        {

            dialog=new LoadingDialog(getWindow(getRoot()), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

        }
        dialog.setVisible(true);
    }


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
