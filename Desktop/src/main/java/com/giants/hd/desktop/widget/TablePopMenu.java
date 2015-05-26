package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.model.BaseTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 自定义表格右键弹出菜单
 */

public class TablePopMenu extends JPopupMenu {



    private JTable table;
    public TablePopMenu(JTable table)
    {
        super();
        this.table=table;
        init();
    }

    private void init() {


        JMenuItem insertItem = new JMenuItem("添加行");
        JMenuItem deleteItem = new JMenuItem("删除行");
        add(insertItem);
        add(deleteItem);
        insertItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                ;
                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
                if (table.getModel() instanceof BaseTableModel) {
                    ((BaseTableModel)table.getModel()).addNewRow(modelIndex);
                }


            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableRow=table.getSelectedRow();
                if(tableRow<0||tableRow>table.getRowCount()-1)
                {
                    JOptionPane.showMessageDialog(table,"请选择行进行删除。");
                }
                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
                if (table.getModel() instanceof BaseTableModel) {
                    ((BaseTableModel)table.getModel()).deleteRow(modelIndex);
                }


            }
        });


    }


}
