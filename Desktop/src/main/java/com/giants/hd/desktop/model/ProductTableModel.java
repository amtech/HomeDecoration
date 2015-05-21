package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Product;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * 产品表格模型
 */

public class ProductTableModel extends BaseTableModel<Product> {
    public static String[] columnNames = new String[]{"图片", "货号", "规格", "单位", "类别", "路径"};
    public static String[] fieldName = new String[]{"photo", "name", "spec", "pTypeName", "pClassName", "url"};

    public  static Class[] classes = new Class[]{ImageIcon.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public ProductTableModel() {
        super(columnNames,fieldName,classes,Product.class);
    }




    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        Product product = getItem(rowIndex);
        if (columnIndex == 0) {

            if (product.getPhoto() == null) {
                return null;
            } else
                return new ImageIcon(product.getPhoto());

        } else

        return       super.getValueAt(rowIndex,columnIndex);

    }


}
