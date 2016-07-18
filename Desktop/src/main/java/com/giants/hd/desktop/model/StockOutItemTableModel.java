package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 出库单细项表格模型
 */

public class StockOutItemTableModel extends BaseTableModel<ErpStockOutItem> {


    public static String[] columnNames = new String[]{ "图片",                                "货号",       "版本号","合同号", "客号",    "客号订单号", "单位","单价","数量" ,  "金额"  ,"每箱套数",  "箱数"    ,"箱规"    ,"箱规体积" ,"总体积",  "净重" ,"毛重","产品描述"};
    public static int[] columnWidth=new int[]{   ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH,    120,          120,      60,       120,       100,       60        ,  60 ,   60,        60 ,     60          ,60  ,      80    , 100         ,    100        ,60    ,  60     ,120};
    public static String[] fieldName = new String[]{"photo",                                   "prd_no",  "id_no","os_no",   "bat_no","cus_os_no", "unit"  , "up",  "qty",     "amt", "so_zxs"     ,"xs"  ,  "khxg","xgtj",        "zxgtj",    "jz1",  "mz"   ,"describe"};

    public  static Class[] classes = new Class[]{ImageIcon.class,Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};



    @Inject
    public StockOutItemTableModel() {
        super(columnNames,fieldName,classes,ErpStockOutItem.class);
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
    public Object getValueAt(int rowIndex, int columnIndex) {




        return super.getValueAt(rowIndex, columnIndex);
    }
}
