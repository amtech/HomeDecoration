package com.giants.hd.desktop.utils;

import com.giants3.hd.appdata.QRProduct;
import com.giants3.hd.utils.GsonUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * Created by david on 2015/12/19.
 */
public class QRHelper {



    public static BufferedImage generateQRCode(QRProduct qrProduct)
    {
        String content= GsonUtils.toJson(qrProduct);




            QRCodeWriter writer = new QRCodeWriter();
            try {

                Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300,hints);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                BufferedImage bi = new BufferedImage(width, height+300, BufferedImage.TYPE_INT_ARGB);
                bi.createGraphics();
                Graphics2D graphics = (Graphics2D) bi.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, width, height);

                // Paint and save the image using the ByteMatrix
                graphics.setColor(Color.BLACK);


                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {

                        if (bitMatrix.get(x, y)) {
                            graphics.fillRect(x, y, 1, 1);
                        }


                    }
                }

               // graphics.fillRect(0,  height, width, height+100);

                graphics.drawString(qrProduct.name,30,height);


                return bi;


//                ImageIO.write(bi, "PNG", new File("c:\\yourImageName.PNG"));
//                ImageIO.write(bi, "JPEG", new File("c:\\yourImageName.JPG"));
//                ImageIO.write(bi, "gif", new File("c:\\yourImageName.GIF"));
//                ImageIO.write(bi, "BMP", new File("c:\\yourImageName.BMP"));

            }catch (WriterException e) {
                e.printStackTrace();
            }

            return null;
        }
}
