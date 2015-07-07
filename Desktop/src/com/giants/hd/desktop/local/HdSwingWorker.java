package com.giants.hd.desktop.local;

import com.giants.hd.desktop.view.LoadingDialog;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;

/**
 * 自定义worker 类 封装 进度条等方法。
 */
public abstract class HdSwingWorker<T,V>  extends SwingWorker<RemoteData<T>,V> {
      LoadingDialog dialog ;




    public HdSwingWorker(Window component)
    {

       this(component,null);
    }

    public HdSwingWorker(Window component,String message)
    {




        dialog  = new LoadingDialog(component,message, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { cancel(true
            );

            }
        });
    }


    @Override
    protected void done() {
        super.done();

        RemoteData<T> result= null;
        HdException exception=null;
        try {
            result = get();
        } catch (InterruptedException e) {


            exception=getCauseException(e);

          //  exception=HdException.create(e.getCause());
            e.printStackTrace();
        } catch (ExecutionException e) {
            exception=getCauseException(e);
         //   exception=HdException.create(e.getCause());
            e.printStackTrace();
        }

        dialog.setVisible(false);
        dialog.dispose();

        if(exception==null) {
            onResult(result);
        }else
        {
            onHandleError(exception);
        }
    }



    private HdException getCauseException(Throwable t)
    {

        Throwable newT=t;
        do{

            if(newT instanceof HdException)
            {
                return (HdException)newT;

            }
            newT=newT.getCause();
        } while(newT!=null);

        return HdException.create(t);


    }

    /**
     * 启动任务bu
     */
    public void go()
    {

        execute();
        dialog.setVisible(true);

    }

    public void onHandleError(HdException exception)
    {


        //TODO  处理线程异常


        String errorMessage="";
        if(exception.getCause()!=null) {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            exception.getCause().printStackTrace(new PrintStream(byteArrayOutputStream));
            errorMessage=byteArrayOutputStream.toString();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            errorMessage=exception.message;

        }

        JTextArea jTextArea=new JTextArea(errorMessage);
        jTextArea.setLineWrap(true);


        JScrollPane jScrollPane=new JScrollPane(jTextArea);
        jScrollPane.setPreferredSize(new Dimension(800,500));
        JOptionPane.showMessageDialog(null,jScrollPane);




    }

    public    abstract void onResult(RemoteData<T> data);

}
