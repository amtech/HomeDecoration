package com.giants.hd.desktop;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.HdPictureWorker;
import com.giants3.hd.domain.api.HttpUrl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ImageViewDialog extends JDialog {
    private JPanel contentPane;
    private JLabel picture;

    private ImageViewDialog(Window frame) {

        super(frame);
        setContentPane(contentPane);

        setModal(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Dimension dimension=   java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(600, 200));
        setLocation((dimension.width - 600) / 2, (dimension.height - 200) / 2);

    }






    public  void loadImageAndShow(final String url)
    {


        picture.setText("正在加载图片....");

       final Dimension dimension=   java.awt.Toolkit.getDefaultToolkit().getScreenSize();



       new HdPictureWorker(new Iconable() {
           @Override
           public void setIcon(ImageIcon icon) {

               if(icon==null)
               {

                   picture.setText("图片加载失败");

               }else
               {

                   picture.setIcon(icon);



                   setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
                   setLocation((dimension.width - icon.getIconWidth()) / 2, (dimension.height -  icon.getIconHeight()) / 2);
               }




           }
       },dimension.getWidth(),dimension.getHeight(),url).execute();



        setVisible(true);

    }

    /**
     * 显示图片显示框体框
     * @param materialCode
     */
    public static void showMaterialDialog(Window frame,String materialCode,String classId) {



        String url= HttpUrl.loadMaterialPicture(materialCode, classId);
         showDialog(frame, url);

    }

    /**
     * 显示图片显示框体框
     * @param productName
     */
    public static void showProductDialog(Window frame,String productName,String version) {



        String url=HttpUrl.loadProductPicture(productName, version);

        showDialog(frame,url,productName+"-"+version);

    }

    /**
     * 显示图片显示框体框
     * @param url
     */
    public static void showDialog(Window frame,String url) {
        showDialog(frame, url, url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));

    }

    /**
     * 显示图片显示框体框
     * @param url
     */
    public static void showDialog(Window frame,String url,String title) {
        ImageViewDialog dialog=new ImageViewDialog(frame);
        dialog.setTitle(title);
        dialog.loadImageAndShow(url);


    }



}
