package com.giants.hd.desktop;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon) {
                picture.setText("");
                picture.setIcon(icon);
                setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
                setLocation((dimension.width - icon.getIconWidth()) / 2, (dimension.height -  icon.getIconHeight()) / 2);

            }

            @Override
            public void onError(String message) {
                picture.setText(message);
            }
        },url,dimension.getWidth(),dimension.getHeight());





        setVisible(true);

    }

    /**
     * 显示图片显示框体框
     * @param materialUrl
     */
    public static void showMaterialDialog(Window frame,String title,String materialUrl) {



        String url= HttpUrl.loadMaterialPicture(  materialUrl);
         showDialog(frame, url,title);

    }

    /**
     * 显示图片显示框体框
     * @param productName
     */
    public static void showProductDialog(Window frame,String productName,String version,String productUrl) {



        String url=HttpUrl.loadProductPicture(productUrl);

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
