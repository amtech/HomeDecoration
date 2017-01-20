package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.OutFactory;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.List;

/**
 * 客户路表格数据模型
 */
public class OutFactoryModel extends  BaseTableModel<OutFactory> {

    public static String[] columnNames = new String[]{                    "名称",  "负责人"  ,"电话","地址" };
    public static int[] columnWidth=new int[]{        100 ,100, 100,ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{  "name",  "manager","telephone","address"};

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public OutFactoryModel( ) {
        super(columnNames, fieldName, classes, OutFactory.class);
        setRowAdjustable(true);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        OutFactory outFactory=getItem(rowIndex);
        String stringValue=String.valueOf(aValue);
        switch (columnIndex)
        {
            case 0:
                outFactory.name=stringValue;
                break;
            case 1:
                outFactory.manager=stringValue;
                break;
            case 2:
                outFactory.telephone=stringValue;
                break;
            case 3:
                outFactory.address=stringValue;
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


    @Override
    public void setDatas(List<OutFactory> newDatas) {

        MiniRowCount=newDatas.size()+20;
        super.setDatas(newDatas);
    }
}
