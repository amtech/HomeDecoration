package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Authority;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  业务员表格数据模型
 */
public class AuthorityModel extends  BaseTableModel<Authority> {

    public static String[] columnNames = new String[]{    "模块名称 ","查看", "添加", "修改"  ,"删除","导入","导出","审核" };
    public static int[] columnWidth=new int[]{   200,    40,40,   40, 40 ,40, 40 ,40, 40  };

    public static String[] fieldName = new String[]{ "module","viewable", "addable",  "editable","deletable","importable","exportable","checkable"};

    public  static Class[] classes = new Class[]{Object.class,Boolean.class, Boolean.class, Boolean.class, Boolean.class ,Boolean.class, Boolean.class,Boolean.class };


    @Inject
    public AuthorityModel() {
        super(columnNames, fieldName, classes, Authority.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if(columnIndex==0) return false;

        return true;
    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Authority authority=getItem(rowIndex);
        boolean booleanValue=false;
        try {
            booleanValue=Boolean.valueOf(aValue.toString().trim());
        }catch (Throwable t){}


        switch (columnIndex)
        {
            case 1:
                authority.viewable=booleanValue;
                break;
            case 2:
            authority.addable=booleanValue;

            break;
            case 3:
                authority.editable=booleanValue;
                break;
            case 4:
                authority.deletable=booleanValue;
                break;
            case 5:
                authority.importable=booleanValue;
                break;
            case 6:
                authority.exportable=booleanValue;
                break;
            case 7:
                authority.checkable=booleanValue;
                break;

        }

        fireTableCellUpdated(rowIndex,columnIndex);
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
