package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemFixColumnTableModel extends  BaseTableModel<QuotationItem> {




    public static String[] columnNames = new String[]{"序号","图片",                                 "品名", "配方号" };
    public static int[] columnWidths = new int []{   40,  ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT,    100,        60 };
    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"photoUrl", "productName", "pVersion" };
    public  static Class[] classes = new Class[]{Object.class,ImageIcon.class, Product.class,Product.class};

    public  static boolean[] editables = new boolean[]{false,false,                                   true,           true};

    QuoteAuth quoteAuth= CacheManager.getInstance().bufferData.quoteAuth;

    @Inject
    public QuotationItemFixColumnTableModel() {
        super(columnNames,fieldName,classes,QuotationItem.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


       QuotationItem item= getItem(rowIndex);
        if( item.productId<=0)
            return "";

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
