package com.giants.hd.desktop.model;

import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.QuotationXKItem;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemXKTableModel extends  BaseTableModel<QuotationXKItem>  {

    public static final String COLUMN_SPEC="spec";
    public static final String COLUMN_SPEC2="spec2";
    public static final String COLUMN_COST="cost";
    public static final String COLUMN_COST2="cost2";

    public static final String COLUMN_PRICE="price";
    public static final String COLUMN_PRICE2="price2";



    public static final String COLUMN_PRODUCT="productName";

    QuoteAuth quoteAuth= CacheManager.getInstance().bufferData.quoteAuth;
    public static String[] columnNames = new String[]{   "*内盒*","*每箱数*",                "客户箱规","单位","成本价", "FOB", "立方数","净重",       "货品规格","材质","镜面尺寸","备注"

                                                            ,                                                                            "*内盒*","*每箱数*",                "客户箱规","单位","成本价", "FOB", "立方数","净重",       "货品规格","材质","镜面尺寸","备注"

    };
    public static int[] columnWidths = new int []{       50,       60,                       120,     40,    50  ,    50,     50,      50,          100,       120,     80,   300,
                                                                                                                                    50,       60,                       120,     40,    50  ,    50,     50,      50,          100,       120,     80,    300};

    public static final String MEMO = "memo";
    public static final String MEMO2 = "memo2";
    public static String[] fieldName = new String[]{ "inBoxCount", "packQuantity", "packageSize","unit",COLUMN_COST,COLUMN_PRICE, "volumeSize","weight",COLUMN_SPEC,   "constitute", "mirrorSize", MEMO,

                                                                                                                            "inBoxCount2", "packQuantity2", "packageSize2","unit2",COLUMN_COST2,COLUMN_PRICE2, "volumeSize2","weight2",COLUMN_SPEC2,   "constitute2", "mirrorSize2", MEMO2};


public  static Class[] classes = new Class[]{ };

    public  static boolean[] editables = new boolean[]{ false,       false,             false,     false,false, false , false,         false,   false,    false,        false,       true ,

                                                                                                                                  false,       false,             false,     false,false, false , false,         false,   false,    false,        false,       true };



    @Inject
    public QuotationItemXKTableModel() {
        super(columnNames,fieldName,classes,QuotationXKItem.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {


        if(quoteAuth.fobEditable&&( fieldName[columnIndex].equals(COLUMN_PRICE) ||fieldName[columnIndex].equals(COLUMN_PRICE2)))
        {

                return  true ;
        }


         return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        QuotationXKItem item = getItem(rowIndex);
        if (item.productId <= 0 && item.productId2 <= 0)
            return "";





        if (fieldName[columnIndex].equals(COLUMN_PRODUCT))
        {
            if(item.productId>0)
                return item.productName;
            if(item.productId2>0)
                return item.productName2;
            return "";

        }

        if((fieldName[columnIndex].equals(COLUMN_COST)||fieldName[columnIndex].equals(COLUMN_COST2))&&!quoteAuth.costVisible)
        {
            return "***";
        }


        if((fieldName[columnIndex].equals(COLUMN_PRICE)||fieldName[columnIndex].equals(COLUMN_PRICE2))&&!quoteAuth.fobVisible)
        {
            return "***";
        }

        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        QuotationXKItem quotationItem=getItem(rowIndex);


        switch (columnIndex )
        {


            case 5:

                quotationItem.price= FloatHelper.scale(Float.valueOf(aValue.toString()));
                break;

            case 17:

                quotationItem.price2=FloatHelper.scale(Float.valueOf(aValue.toString()));
                break;
            case 11:

            quotationItem.memo=aValue.toString();
            break;
            case 23:

                quotationItem.memo2=aValue.toString();
                break;
        }




        fireTableRowsUpdated(rowIndex,rowIndex);


    }




    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC),ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC2),ArrayUtils.indexOnArray(fieldName, MEMO),ArrayUtils.indexOnArray(fieldName, MEMO2)};
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
