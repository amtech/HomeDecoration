package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 产品表格模型
 */

public class ProductTableModel extends BaseTableModel<Product> {


    public static final String COLUMN_SPEC="spec";

    public static final String COLUMN_SHOW_COST="column_show_cost";
    public static String[] columnNames = new String[]{"图片", "货号","版本号","包装类型", "规格", "单位", "类别", "日期","材料成本","备注"};
    public static int[] columnWidth=new int[]{ ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 60, 100,200,40, 60,120 ,    120, ConstantData.MAX_COLUMN_WIDTH};
    public static String[] fieldName = new String[]{"photo", "name",  "pVersion","pack",COLUMN_SPEC,"pUnitName", "pClassName", "rDate",COLUMN_SHOW_COST,"memo"};

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

    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName,COLUMN_SPEC),ArrayUtils.indexOnArray(fieldName,COLUMN_SHOW_COST)};
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Product product=getItem(rowIndex);
        if(product.id<=0) return null;

        //显示成本简易显示信息
        if(COLUMN_SHOW_COST.equals(fieldName[columnIndex]))
        {



            String value="";
            value+="白胚 : "+product.conceptusCost+ StringUtils.row_separator;
            value+="组装 : "+product.assembleCost+ StringUtils.row_separator;
            value+="油漆 : "+product.paintCost+ StringUtils.row_separator;
            value+="包装 : "+product.packCost+ StringUtils.row_separator;

            return value;




        }else

        return super.getValueAt(rowIndex, columnIndex);
    }
}
