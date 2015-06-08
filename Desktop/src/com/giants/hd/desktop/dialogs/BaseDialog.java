package com.giants.hd.desktop.dialogs;

import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;

/**
 * 所有对话框类的基类 提供注射功能
 *
 */
public class BaseDialog<T> extends JDialog{



    protected  T result;
    public BaseDialog(Window window)
    {
        this(window, "默认对话框");
    }

    public BaseDialog(Window owner, String title) {
        super(owner, title);
        dialogInit();
    }

    @Override
    protected  void dialogInit()
     {
         super.dialogInit();
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
