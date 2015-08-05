package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.entity.Product;

import com.giants3.hd.utils.entity.Product2;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.entity.QuotationXKItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemXKTableModel extends  BaseTableModel<QuotationXKItem> implements  Productable {

    public static final String COLUMN_SPEC="spec";
    public static final String COLUMN_SPEC2="spec2";
    public static final String COLUMN_COST="cost";
    public static final String COLUMN_COST2="cost2";
    public static String[] columnNames = new String[]{"序号","图片",                                 "品名", "配方号",   "配方号(加强)",  "*内盒*","*每箱数*",                "客户箱规","单位","成本价", "FOB", "立方数","净重",       "货品规格","材质","镜面尺寸","备注"

                                                            ,                                                                            "*内盒*","*每箱数*",                "客户箱规","单位","成本价", "FOB", "立方数","净重",       "货品规格","材质","镜面尺寸","备注"

    };
    public static int[] columnWidths = new int []{   40,  ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT,    100,        60,       60,     50,       60,                       120,     40,    50  ,    50,     50,      50,          100,       120,     80,   300,
                                                                                                                                    50,       60,                       120,     40,    50  ,    50,     50,      50,          100,       120,     80,    300};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"productPhoto", "productName", "pVersion", "pVersion2" ,"inBoxCount", "packQuantity", "packageSize","unit",COLUMN_COST,"price", "volumeSize","weight",COLUMN_SPEC,   "constitute", "mirrorSize","memo",

                                                                                                                            "inBoxCount2", "packQuantity2", "packageSize2","unit2",COLUMN_COST2,"price2", "volumeSize2","weight2",COLUMN_SPEC2,   "constitute2", "mirrorSize2","memo2"};


public  static Class[] classes = new Class[]{Object.class,ImageIcon.class, Product.class,Product.class,Product2.class};

    public  static boolean[] editables = new boolean[]{false,false,                                   true,           true, true,false,       false,             false,     false,false, true , false,         false,   false,    false,        false,       true ,

                                                                                                                                  false,       false,             false,     false,false, true , false,         false,   false,    false,        false,       true };



    @Inject
    public QuotationItemXKTableModel() {
        super(columnNames,fieldName,classes,QuotationXKItem.class);
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

        QuotationXKItem quotationItem=getItem(rowIndex);


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
        return new int[]{ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC),ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC2)};
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


        QuotationXKItem item=getItem(rowIndex);

        item.updateProduct(product);


        fireTableRowsUpdated(rowIndex,rowIndex);

        }


public void setProduct2(Product product, int rowIndex) {


        QuotationXKItem item=getItem(rowIndex);
        item.updateProduct2(product);
        fireTableRowsUpdated(rowIndex,rowIndex);

    }
}
