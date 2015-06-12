package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.JTableUtils;
import com.giants.hd.desktop.model.BaseTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 *  自定义表格
 *
 *  拦截setModel 方法， 满足一定条件 定制列宽  行高
 */
public class JHdTable extends JTable {


    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);

        if(dataModel instanceof BaseTableModel)
        {

            BaseTableModel baseTableModel=(BaseTableModel)dataModel;
            int[] columnWidths=baseTableModel.getColumnWidth();
            JTableUtils.setJTableColumnsWidth(this,columnWidths);


            int rowHeight=baseTableModel.getRowHeight();
            if(rowHeight>0)
            {
                setRowHeight(rowHeight);
            }
        }



    }
}
