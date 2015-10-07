package com.giants.hd.desktop.local;

import com.giants.hd.desktop.dialogs.LoginDialog;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.viewImpl.LoadingDialog;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by david on 2015/9/22.
 */
public class HdPictureWorker  extends  SwingWorker<BufferedImage,Object>
{


   private   Iconable iconable;
    double destWidth;
    double destHeight;
    String url;



    public HdPictureWorker(Iconable iconable,double width,double height,String url)
    {

        this.iconable=iconable;
        this.destWidth=width;
        this.destHeight=height;
        this.url=url;
    }


    protected void done() {
        super.done();


        ImageIcon imageIcon = null;
        BufferedImage image = null;
        try {
            image = get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (null != image) {


            int width = image.getWidth();
            int height = image.getHeight();


            while (!(width < destWidth && height < destHeight)) {
                width = (int) (width / 1.1f);
                height = (int) (height / 1.1f);
            }


            try {
                BufferedImage newImage = resizeImage(image, width, height, image.getType());

                image = newImage;

            } catch (IOException e) {
                e.printStackTrace();
            }
            imageIcon = new ImageIcon(image);


        }
        iconable.setIcon(imageIcon);

    }


        @Override
        protected BufferedImage doInBackground() throws Exception {
            return ImageIO.read(new URL(url));
        }



        private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {
            BufferedImage resizedImage = new BufferedImage(width, height, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            return resizedImage;
        }
}
