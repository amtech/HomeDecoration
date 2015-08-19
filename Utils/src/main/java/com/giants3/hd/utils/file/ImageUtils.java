package com.giants3.hd.utils.file;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.exception.HdException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 图片处理的功能
 */

public class ImageUtils {

    public static final String TAG="ImageUtils";

    //产品微缩图尺寸
    public static final int MAX_PRODUCT_MINIATURE_WIDTH =100;
    public static final int MAX_PRODUCT_MINIATURE_HEIGHT =100;

    //材料微缩图尺寸
    public static final int MAX_MATERIAL_MINIATURE_WIDTH =50;
    public static final int MAX_MATERIAL_MINIATURE_HEIGHT =50;

    /**
     * 微缩产品图片
     * @param path
     * @return
     * @throws HdException
     */
    public static final byte[] scaleProduct(String path) throws HdException {

        return scale(path, MAX_PRODUCT_MINIATURE_WIDTH, MAX_PRODUCT_MINIATURE_HEIGHT);


    }


    /**
     * 微缩材料图片
     * @param path
     * @return
     * @throws HdException
     */
    public static final byte[] scaleMaterial(String path) throws HdException {

        return scale(path, MAX_MATERIAL_MINIATURE_WIDTH, MAX_MATERIAL_MINIATURE_HEIGHT);




    }

    public static final byte[] scale(String path, int maxWidth, int maxHeight) throws HdException {

       return scale(path, maxWidth, maxHeight, false);


    }


    /**
     * 生成缩略图的字节流
     *
     * 设定最高宽高， 等比例压缩
     *
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(String filePath, int maxWidth, int maxHeight, boolean preserveAlpha) throws HdException {

        try {



            File file=new File(filePath);
            BufferedImage img = ImageIO.read(file);

            int sourceWidth=img.getWidth();
            int sourceHeight=img.getHeight();
            //计算缩放比例
            float ratio=Math.max((float)sourceWidth/maxWidth,(float)sourceHeight/maxHeight);

            ratio=Math.max(ratio,1);
            int newWidth= (int) (sourceWidth/ratio);
            int newHeight= (int) (sourceHeight/ratio);


            Logger.getLogger(TAG).info("scaleProduct Image----newWidth:"+newWidth+",newHeight:"+newHeight);
            int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

            BufferedImage imageBuff = new BufferedImage(newWidth, newHeight, imageType);

            Graphics2D g = imageBuff.createGraphics();
            if(preserveAlpha)
                g.setComposite(AlphaComposite.Src);

          //   g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            g.drawImage(img, 0, 0, newWidth, newHeight, null);
            g.dispose();

            img.flush();


            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, preserveAlpha ? "png" : "jpg", buffer);
            imageBuff.flush();
            byte[] result= buffer.toByteArray();
            buffer.close();
            return result;
        } catch (IOException e) {
            throw HdException.create(HdException.FAIL_SCALE_IMAGE,e);
        }
    }

    public static boolean isPictureFile(String fileName) {


        if(StringUtils.isEmpty(fileName)) return false;
        String lowerCaseValue=fileName.toLowerCase();

     return lowerCaseValue.endsWith(".jpg")||lowerCaseValue.endsWith(".jpeg")||lowerCaseValue.endsWith(".png")  ;
    }
}
