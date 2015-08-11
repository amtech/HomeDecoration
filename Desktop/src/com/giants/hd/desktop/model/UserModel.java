package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class UserModel extends  BaseTableModel<User> {

    public static String[] columnNames = new String[]{ "编号 ",  "名称" ,"中文名称      ","密码" ,"电话","邮件","是否业务员" };
    public static int[] columnWidth=new int[]{   80,        100 ,120,120,120,120,40};

    public static String[] fieldName = new String[]{ "code", "name", "chineseName", "password","tel","email","isSalesman"};

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class, Object.class, Object.class,Object.class,Boolean.class};


    @Inject
    public UserModel() {
        super(columnNames, fieldName, classes, User.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        User customer=getItem(rowIndex);
        String stringValue=String.valueOf(aValue);
        switch (columnIndex)
        {
            case 0:
                customer.code=stringValue;
                break;
            case 1:
                customer.name=stringValue;
                break;
            case 2:
                customer.chineseName=stringValue;
                break;
            case 3:
                customer.password=stringValue;
                break;

            case 4:
                customer.tel=stringValue;
                break;
            case 5:
                customer.email=stringValue;
                break;
            case 6:
                customer.isSalesman=Boolean.valueOf(stringValue);
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
