package com.giants.hd.desktop.local;

import com.giants.hd.desktop.interf.Iconable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import com.google.inject.Guice;
import com.google.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 图片加载类
 */
public class ImageLoader {
    private static ImageLoader ourInstance = null;

    public static synchronized ImageLoader getInstance() {

        if(ourInstance==null)
        {
            ourInstance=new ImageLoader();
        }
        return ourInstance;
    }

    DownloadFileManager manager;

    private ImageLoader() {


        manager= Guice.createInjector().getInstance(DownloadFileManager.class);

    }
    public void displayImage(final Iconable iconable,final String url)
    {


        displayImage(iconable,url,120,120);

    }


    public void displayImage(final Iconable iconable,final String url, final double maxWidth, final double maxHeight)
    {


          Observable.create(new Observable.OnSubscribe<BufferedImage>() {
            @Override
            public void call(Subscriber<? super BufferedImage> subscriber) {

                try {
                    String fileName = manager.cacheFile(url);
                    subscriber.onNext(ImageIO.read(new File(fileName)));
                    subscriber.onCompleted();
                } catch (IOException e) {

                    subscriber.onError(e);
                }


            }
        }).map(new Func1<BufferedImage, BufferedImage>() {
              @Override
              public BufferedImage call(BufferedImage bufferedImage) {
                  return scaleImage(bufferedImage, maxWidth, maxHeight);



              }
          })
                  .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.immediate()).subscribe(new Observer<BufferedImage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
                iconable.onError("图片加载失败");
            }

            @Override
            public void onNext(BufferedImage bufferedImage) {


                iconable.setIcon(new ImageIcon(bufferedImage));

            }
        });




    }

    private BufferedImage scaleImage(BufferedImage bufferedImage, double maxWidth, double maxHeight) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();


        while (!(width < maxWidth && height < maxHeight)) {
            width = (int) (width / 1.1f);
            height = (int) (height / 1.1f);
        }


        BufferedImage newImage = null;
        try {
            newImage = ImageUtils.resizeImage(bufferedImage, width, height, bufferedImage.getType());

            bufferedImage.flush();
            return newImage;

        } catch (IOException e) {
            e.printStackTrace();
            return bufferedImage;

        }
    }


    public String cacheFile(String url) throws IOException {


            return manager.cacheFile(url);

    }




    public BufferedImage loadImage(String url)  throws IOException
    {
       return loadImage(url,-1,-1);
    }


    public BufferedImage loadImage(String url,final double maxWidth, final double maxHeight)  throws IOException
    {
        String fileName=manager.cacheFile(url);

        if(maxHeight<=0||maxWidth<=0)
            return ImageIO.read(new File(fileName));

         return  scaleImage(  ImageIO.read(new File(fileName)),maxWidth,maxHeight);



    }


    public void clearCache()
    {
          manager.clearCache();
    }


}
