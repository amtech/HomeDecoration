package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 产品报价表格模型
 */

public class QuotationTableModel extends BaseTableModel<Quotation> {
    public static String[] columnNames = new String[]{"报价日期", "报价单号","客户", "有效日期","币别", "业务员" ,"报价类型","备注"};
    public static String[] fieldName = new String[]{"qDate",       "qNumber", "customerName", "vDate", "currency", "salesman","quotationTypeName","memo"};
    public static int[] columnWidths = new int []{      120,              100,             100,    120,     80,          100,   120, ConstantData.MAX_COLUMN_WIDTH };


    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public QuotationTableModel() {
        super(columnNames,fieldName,classes,Quotation.class);
    }





    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }

}
