package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Quotation;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 产品报价表格模型
 */

public class QuotationTableModel extends BaseTableModel<Quotation> {
    public static String[] columnNames = new String[]{"图片", "货号", "规格", "单位", "类别", "日期"};
    public static String[] fieldName = new String[]{"photo", "name", "spec", "pUnitName", "pClassName", "rDate"};

    public  static Class[] classes = new Class[]{ImageIcon.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public QuotationTableModel() {
        super(columnNames,fieldName,classes,Quotation.class);
    }




    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        return null;

    }


}
