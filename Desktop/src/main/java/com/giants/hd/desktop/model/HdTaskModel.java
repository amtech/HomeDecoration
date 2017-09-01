package com.giants.hd.desktop.model;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  定时任务数据模型
 */
public class HdTaskModel extends  BaseTableModel<HdTask> {

    public static String[] columnNames = new String[]{                         "任务名称 ",  "运行时刻"  ,"启动人" ,"启动时间","任务说明","重复次数","执行次数"};
    public static int[] columnWidth=new int[]{   150,        150 ,200, 120,200,80,80};

    public static final String REPEAT_COUNT = "repeatCount";
    public static String[] fieldName = new String[]{ "taskName", "dateString", "activator", "activateTime","memo" , REPEAT_COUNT,"executeCount"};

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
    public Object getValueAt(int rowIndex, int columnIndex) {

        HdTask task=getItem(rowIndex);
        if(task.id<=0) return "";

        if(REPEAT_COUNT.equals(fieldName[columnIndex]))
        {


            if(task.repeatCount==Integer.MAX_VALUE)
            {
                return "每日";
            }else
                return task.repeatCount;

        }



        return super.getValueAt(rowIndex, columnIndex);
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
