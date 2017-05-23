package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.IViewer;
import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 基础模本类。  提供guice注射等等功能  提供一些公共方法
 */
public abstract class BasePanel implements IViewer {

    protected PanelListener listener;
    private Window window;


    public PanelListener getListener() {
        return listener;
    }

    public void setListener(PanelListener listener) {
        this.listener = listener;
    }

    //加载进度条 所有的view  只有一个顶级界面挂靠的进度条控件
 private   static LoadingDialog dialog;

    public BasePanel() {

        Guice.createInjector().injectMembers(this);
    }

    public BasePanel(Window window) {

        this.window = window;
        Guice.createInjector().injectMembers(this);
    }


    /**
     * 遍历返回任意控件的frame
     *
     * @param component
     * @return
     */
    protected Window getWindow(Component component) {

        while (component != null && !(component instanceof Window))
            component = component.getParent();
        return (Window) component;
    }


    /**
     * 遍历返回任意控件的frame
     *
     * @param
     * @return
     */
    public Window getWindow() {
        Component component = getRoot();
        if (component == null) {
            throw new RuntimeException(getClass().getCanonicalName() + " did not implements getRoot method and return a component");
        }
        while (component != null && !(component instanceof Window))
            component = component.getParent();
        return (Window) component;
    }


    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public abstract JComponent getRoot();

    public void hideLoadingDialog() {



        if (dialog != null) {
            dialog.setVisible(false);

        }
    }


    @Override
    public void showLoadingDialogCarfully() {



        showLoadingDialog();

    }

    public void showMesssage(String message) {

        JOptionPane.showMessageDialog(getWindow(getRoot()), message);
    }


    public boolean showConfirmMessage(String message) {

        return showConfirmMessage(message, null);
    }

    public boolean showConfirmMessage(String message, String title) {

        return JOptionPane.showConfirmDialog(getWindow(), message, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    @Override
    public void showLoadingDialog() {


        showLoadingDialog("");

    }


    @Override
    public void showLoadingDialog(String hint) {


        //重複多次新建loading dialog 會導致程序一次， 先關閉

//
        if (dialog == null) {

            final JComponent root =SwingUtilities.getRootPane( getRoot());

            System.out.println("root:"+root);

            final Window window = getWindow(root);

            System.out.println("window:"+window);
            dialog = new LoadingDialog(window, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            dialog.setMessage(hint);



        }
        dialog.setVisible(true);
    }


    /**
     * 面板回调接口
     */
    public interface PanelListener {

        public void save();

        public void delete();


        public void close();

        public void verify();

        public void unVerify();
    }


    public static class PanelAdapter implements PanelListener {
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




    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }
}
