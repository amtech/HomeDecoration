package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants.hd.desktop.view.Panel_ProductDetail;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 *  功能辅助类。
 */
public class HdSwingUtils {



    /**
     * 显示产品详情
     * @param product
     * @Param component
     *
     */
    public static  void showDetailPanel(Product product ,Component component)
    {
        UIManager.put("TabbedPane.selected", Color.RED);

        JFrame frame =new ProductDetailFrame(product);
        frame.setLocationRelativeTo(component);
        frame.setVisible(true);
    }


    /**
     * 显示产品详情
     * @param productDetail
     * @Param component
     *
     */
    public static  void showDetailPanel(Component component,ProductDetail productDetail )
    {



        JFrame frame =new ProductDetailFrame(productDetail);
        frame.setLocationRelativeTo(component);
        frame.setVisible(true);
    }


    /**
     * 生成适合JLable 展示的 多行文本
     * @param sourceString
     * @return
     */
    public static String multiLineForLabel(String sourceString)
    {

        String[] valueArray=   sourceString.split(StringUtils.row_separator);

        return multiLineForLabel(valueArray);

    }


    /**
     * 生成适合JLable 展示的 多行文本
     * @param arrayString
     * @return
     */
    public static String multiLineForLabel(String... arrayString)
    {
        String resultToString="<html>";
        for (int i = 0; i < arrayString.length; i++) {
            resultToString+=arrayString[i]+"<br>";
        }

        return  resultToString;

    }
}
