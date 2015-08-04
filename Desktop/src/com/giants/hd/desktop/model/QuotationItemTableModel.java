package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemTableModel extends  BaseTableModel<QuotationItem> implements  Productable {

    public static final String COLUMN_SPEC="spec";
    public static final String COLUMN_COST="cost";
    public static String[] columnNames = new String[]{"序号","图片",                                 "品名", "配方号", "*内盒*","*每箱数*",                "客户箱规","单位","成本价", "FOB", "立方数","净重",       "货品规格","材质","镜面尺寸","备注" };
    public static int[] columnWidths = new int []{   40,  ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT,    100,        60,     50,       60,                       120,     40,    50  ,    50,     50,      50,          100,       120,     80,    ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"productPhoto", "productName", "pVersion", "inBoxCount", "packQuantity", "packageSize","unit",COLUMN_COST,"price", "volumeSize","weight",COLUMN_SPEC,   "constitute", "mirrorSize","memo"};
    public  static Class[] classes = new Class[]{Object.class,ImageIcon.class, Product.class,Product.class};

    public  static boolean[] editables = new boolean[]{false,false,                                   true,           true, false,       false,             false,     false,false, true , false,         false,   false,    false,        false,       true };



    @Inject
    public QuotationItemTableModel() {
        super(columnNames,fieldName,classes,QuotationItem.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {



         return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


//        if(fieldName[columnIndex].equals(COLUMN_COST))
//        {
//            return "***";
//        }
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        QuotationItem quotationItem=getItem(rowIndex);


        switch (columnIndex )
        {

            case 9:

                quotationItem.price=Float.valueOf(aValue.toString());
                break;
            case 15:

                quotationItem.memo=aValue.toString();
                break;
        }




        fireTableRowsUpdated(rowIndex,rowIndex);


    }




    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC)};
    }





    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }

    @Override
    public void setProduct(Product product, int rowIndex) {


        QuotationItem item=getItem(rowIndex);


        item.updateProduct(product);


        fireTableRowsUpdated(rowIndex,rowIndex);

    }
}
