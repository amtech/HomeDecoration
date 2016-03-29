package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 订单表格模型
 */

public class OrderItemTableModel extends BaseTableModel<ErpOrderItem> {




    public static String[] columnNames = new String[]{"序号","图片", "货号","客号","货号",  "单位", "单价", "数量","金额","箱数","每箱数","箱规","立方数","总立方数","产品尺寸","备注"};
    public static int[] columnWidth=new int[]{ 40, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 60, 100,200,40, 60,120 ,    120, 80,80,120,120,120,120,ConstantData.MAX_COLUMN_WIDTH};
    public static String[] fieldName = new String[]{"itm","url", "prd_no",  "bat_no","prd_name","ut", "up", "qty","amt","htxs","so_zxs","khxg","xgtj","zxgtj","hpgg","memo"};

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};



    @Inject
    public OrderItemTableModel() {
        super(columnNames,fieldName,classes,ErpOrderItem.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }




//
}
