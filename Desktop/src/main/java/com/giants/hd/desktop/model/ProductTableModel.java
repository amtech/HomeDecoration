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
    public String[] columnNames = new String[]{"图片", "货号", "规格", "单位", "类别", "路径"};
    public String[] fieldName = new String[]{"photo", "name", "spec", "pTypeName", "pClassName", "url"};
    public Field[] fields;

    public Class[] classes = new Class[]{ImageIcon.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public ProductTableModel() {
        super();


        int size = fieldName.length;

        fields = new Field[size];
        for (int i = 0; i < size; i++) {

            try {
                fields[i] = Product.class.getField(fieldName[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }


    @Override
    public Class getColumnClass(int c) {


        return classes[c];

    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (fields[columnIndex] == null) return null;


        Product product = getItem(rowIndex);

        if (columnIndex == 0) {

            if (product.getPhoto() == null) {
                return null;
            } else
                return new ImageIcon(product.getPhoto());

        } else

            try {
                return fields[columnIndex].get(product);
            } catch (IllegalAccessException e) {
                // e.printStackTrace();
            }


        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
