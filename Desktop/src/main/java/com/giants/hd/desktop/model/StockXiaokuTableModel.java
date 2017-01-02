package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.StockXiaoku;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 销库格数据模型
 */
public class StockXiaokuTableModel extends BaseTableModel<StockXiaoku> {

    public static String[] columnNames = new String[]{"单号","   柜数 ", "  柜号 ","  封签号 ", " 拖车公司 ", "", " 发票号", "柜型"};
    public static int[] columnWidth = new int[]{100,60, 100, 100,100,100,100,100};


    public static String[] fieldName = new String[]{"ps_no","xhdg","xhgh", "xhfq", "tcgs", "", "xhph", "xhgx"};

    public static Class[] classes = new Class[]{Object.class, Number.class};


    @Inject
    public StockXiaokuTableModel() {
        super(columnNames, fieldName, classes, StockXiaoku.class);
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }
}
