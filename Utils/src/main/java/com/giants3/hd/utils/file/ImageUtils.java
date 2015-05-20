package com.giants3.hd.utils.file;

import com.giants3.hd.utils.exception.HdException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * ͼƬ����Ĺ���
 */

public class ImageUtils {

    public static final int MAX_MINIATURE_WIDTH=20;
    public static final int MAX_MINIATURE_HEIGHT=20;



    public static final byte[] scale(String path) throws HdException {

        return scale(path,MAX_MINIATURE_WIDTH,MAX_MINIATURE_HEIGHT);




    }

    public static final byte[] scale(String path,int maxWidth, int maxHeight) throws HdException {

       return scale(path,maxWidth,maxHeight,false);




    }


    /**
     * ��������ͼ���ֽ���
     *
     * �趨��߿�ߣ� �ȱ���ѹ��
     *
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(String filePath, int maxWidth, int maxHeight,boolean preserveAlpha) throws HdException {

        try {
            BufferedImage img = ImageIO.read(new File(filePath));

            int sourceWidth=img.getWidth();
            int sourceHeight=img.getHeight();
            //�������ű���
            float ratio=Math.max((float)sourceWidth/maxWidth,(float)sourceHeight/maxHeight);

            ratio=Math.min(ratio,1);

            int newWidth= (int) (sourceWidth/ratio);
            int newHeight= (int) (sourceHeight/ratio);



           Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

            BufferedImage imageBuff = new BufferedImage(newWidth, newHeight, imageType);

            Graphics2D g = imageBuff.createGraphics();
            if(preserveAlpha)
                g.setComposite(AlphaComposite.Src);

          //   g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            g.drawImage(img, 0, 0, newWidth, newHeight, null);

            g.dispose();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, preserveAlpha ? "png" : "jpg", buffer);





            byte[] result= buffer.toByteArray();
            buffer.close();
            return result;
        } catch (IOException e) {
            throw HdException.create(HdException.FAIL_SCALE_IMAGE,e);
        }
    }

}
