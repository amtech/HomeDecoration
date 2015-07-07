package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.Salesman;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class SalesmanModel extends  BaseTableModel<Salesman> {

    public static String[] columnNames = new String[]{                         "编号 ",  "名称"  ,"" };
    public static int[] columnWidth=new int[]{   100,        120 ,ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ "code", "name",  " "};

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public SalesmanModel() {
        super(columnNames, fieldName, classes, Salesman.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Salesman customer=getItem(rowIndex);
        String stringValue=String.valueOf(aValue);
        switch (columnIndex)
        {
            case 0:
                customer.code=stringValue;
                break;
            case 1:
                customer.name=stringValue;
                break;

        }
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
