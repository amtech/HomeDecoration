package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.utils.JTableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
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


            //配置定制列宽
            BaseTableModel baseTableModel=(BaseTableModel)dataModel;
            int[] columnWidths=baseTableModel.getColumnWidth();
            JTableUtils.setJTableColumnsWidth(this,columnWidths);

            //配置行高
            int rowHeight=baseTableModel.getRowHeight();
            if(rowHeight>0)
            {
                setRowHeight(rowHeight);
            }

            //配置多行文本展示。
            int[] multiLineTextColumnIndexes=baseTableModel.getMultiLineColumns();

            if(multiLineTextColumnIndexes!=null&&multiLineTextColumnIndexes.length>0) {

                DefaultTableCellRenderer renderer=      new DefaultTableCellRenderer() {


                    @Override
                    protected void setValue(Object value) {

                        String valueString = value == null ? "" : value.toString();
                        super.setValue(HdSwingUtils.multiLineForLabel(valueString));
                    }
                };
                TableColumnModel columnModel = getColumnModel();
                for(int index:multiLineTextColumnIndexes)
                {
                    TableColumn column = columnModel.getColumn(index);
                    column.setCellRenderer(renderer);
                }



            }
        }



    }


}
