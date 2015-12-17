package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.HdTask;
import com.giants3.hd.utils.entity.Module;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  定时任务数据模型
 */
public class HdTaskModel extends  BaseTableModel<HdTask> {

    public static String[] columnNames = new String[]{                         "任务名称 ",  "运行时刻"  ,"启动人" ,"启动时间","备注"};
    public static int[] columnWidth=new int[]{   150,        150 ,200, 120, ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ "taskName", "dateString", "activator", "activateTime","memo"};

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public HdTaskModel() {
        super(columnNames, fieldName, classes, HdTask.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
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
