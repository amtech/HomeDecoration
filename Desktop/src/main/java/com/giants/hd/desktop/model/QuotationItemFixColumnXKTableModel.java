package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.QuotationXKItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.Product2;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemFixColumnXKTableModel extends  BaseTableModel<QuotationXKItem>  {




    public static final String COLUMN_PRODUCT="productName";
    public static final String COLUMN_PRODUCT_PHOTO="productPhoto";

    public static String[] columnNames = new String[]{"序号","图片",                                 "品名", "配方号(折叠)",   "配方号(加强)" } ;
    public static int[] columnWidths = new int []{   40,  ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT,    100,        60,       60};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,COLUMN_PRODUCT_PHOTO, COLUMN_PRODUCT, "pVersion", "pVersion2"  };

public  static Class[] classes = new Class[]{Object.class,ImageIcon.class, String.class,Product.class,Product2.class};

    public  static boolean[] editables = new boolean[]{false,false,                                   false,           true, true };


    @Inject
    public QuotationItemFixColumnXKTableModel() {
        super(columnNames,fieldName,classes,QuotationXKItem.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {





         return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        QuotationXKItem item = getItem(rowIndex);
        if (item.productId <= 0 && item.productId2 <= 0)
            return "";


        if (fieldName[columnIndex].equals(COLUMN_PRODUCT_PHOTO))
        {
            if(item.productPhoto!=null)
                   return new ImageIcon(item.productPhoto);
            if(item.productPhoto2!=null)
                return new ImageIcon(item.productPhoto2);
            return null;

        }


        if (fieldName[columnIndex].equals(COLUMN_PRODUCT))
        {
            if(item.productId>0)
                return item.productName;
            if(item.productId2>0)
                return item.productName2;
            return "";

        }



        return super.getValueAt(rowIndex, columnIndex);
    }












    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }




}
