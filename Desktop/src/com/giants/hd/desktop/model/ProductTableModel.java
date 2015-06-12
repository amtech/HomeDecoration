package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 产品表格模型
 */

public class ProductTableModel extends BaseTableModel<Product> {
    public static String[] columnNames = new String[]{"图片", "货号","版本号","包装类型", "规格", "单位", "类别", "日期","备注"};
    public static int[] columnWidth=new int[]{ ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 60, 100,200,40, 60,120, ConstantData.MAX_COLUMN_WIDTH};
    public static String[] fieldName = new String[]{"photo", "name",  "pVersion","pack","spec","pUnitName", "pClassName", "rDate","memo"};

    public  static Class[] classes = new Class[]{ImageIcon.class, Object.class, Object.class, Object.class, Object.class, Object.class};



    @Inject
    public ProductTableModel() {
        super(columnNames,fieldName,classes,Product.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }
}
