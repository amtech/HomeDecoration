package com.giants.hd.desktop.local;

import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants.hd.desktop.view.Panel_ProductDetail;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;

import javax.swing.*;
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
}
