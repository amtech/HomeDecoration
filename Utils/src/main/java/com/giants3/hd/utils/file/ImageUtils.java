package com.giants3.hd.utils.file;

import com.giants3.hd.utils.exception.HdException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理的功能
 */

public class ImageUtils {

    public static final int MAX_MINIATURE_WIDTH=100;
    public static final int MAX_MINIATURE_HEIGHT=100;


    public static final byte[] scale(String path) throws HdException {

        return scale(path,MAX_MINIATURE_WIDTH,MAX_MINIATURE_HEIGHT);




    }

    public static final byte[] scale(String path,int maxWidth, int maxHeight) throws HdException {

       return scale(new File(path),maxWidth,maxHeight);




    }


    /**
     * 生成缩略图的字节流
     * @param file
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(File file, int maxWidth, int maxHeight) throws HdException {

        try {
            BufferedImage img = ImageIO.read(file);

            int sourceWidth=img.getWidth();
            int sourceHeight=img.getHeight();
            //计算缩放比例
            float ratio=Math.max((float)sourceWidth/maxWidth,(float)sourceHeight/maxHeight);

            ratio=Math.min(ratio,1);

            int newWidth= (int) (sourceWidth/ratio);
            int newHeight= (int) (sourceHeight/ratio);



            Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            byte[] result= buffer.toByteArray();
            buffer.close();
            return result;
        } catch (IOException e) {
            throw HdException.create(HdException.FAIL_SCALE_IMAGE,e);
        }
    }

}
