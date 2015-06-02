package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.local.ClipBordHelper;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 自定义表格右键弹出菜单
 */

public class TablePopMenu extends JPopupMenu {



    private JTable table;
    private TableMenuLister lister;


    public static  final int ITEM_INSERT=1;
    public static  final int ITEM_DELETE=2;
    public static  final int ITEM_PAST=3;

    public TablePopMenu(JTable table,TableMenuLister lister)
    {
        super();
        this.table=table;
        this.lister=lister;
        init();
    }

    private void init() {


        JMenuItem insertItem = new JMenuItem("添加行");
        JMenuItem deleteItem = new JMenuItem("删除行");
        JMenuItem pastItem = new JMenuItem("黏贴");
        add(insertItem);
        add(deleteItem);
        add(pastItem);
        insertItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {




                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();

                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_INSERT,model,modelIndex);
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
                    return;
                }
                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();

                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_DELETE,model,modelIndex);
                }


            }
        });

        pastItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {







                int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());

                if (table.getModel() instanceof BaseTableModel) {
                    BaseTableModel model = (BaseTableModel) table.getModel();
                    if(lister!=null)
                        lister.onTableMenuClick(ITEM_PAST ,model,modelIndex);



                }







            }
        });


    }



    public  interface  TableMenuLister
    {
        public void onTableMenuClick(int index, BaseTableModel tableModel,int rowIndex);
    }

}
