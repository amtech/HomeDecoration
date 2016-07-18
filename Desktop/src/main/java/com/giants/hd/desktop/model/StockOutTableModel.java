package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.StockOut;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 出库单表格模型
 */

public class StockOutTableModel extends BaseTableModel<ErpStockOut> {


    public static String[] columnNames = new String[]{  "出库单","出库日期","客户", "目的港", "提单号", "柜数柜型" };
    public static int[] columnWidth=new int[]{   120, 120,60,120,100, 60 };
    public static String[] fieldName = new String[]{  "ck_no",  "ck_dd","cus_no","mdg","tdh", "gsgx" };

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};



    @Inject
    public StockOutTableModel() {
        super(columnNames,fieldName,classes,ErpStockOut.class);
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
