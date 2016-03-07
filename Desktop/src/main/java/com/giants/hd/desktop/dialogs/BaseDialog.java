package com.giants.hd.desktop.dialogs;

import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 所有对话框类的基类 提供注射功能
 *
 */
public class BaseDialog<T> extends JDialog{



    protected  T result;
    public BaseDialog(Window window)
    {
        this(window, "默认对话框");
        setLocationRelativeTo(window);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialogInit();
    }

    public BaseDialog(Window window, String title) {
        super(window, title);
        setLocationRelativeTo(window);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialogInit();
    }

    @Override
    protected  void dialogInit()
     {



         super.dialogInit();
         addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 dispose();

             }
         });
         setMinimumSize(new Dimension(400,400));
         Guice.createInjector().injectMembers(this);
     }


   public  T getResult()
   {

            return result;

   }

    protected void setResult(T result)
    {
        this.result=result;
    }




}
