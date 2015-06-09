package com.giants.hd.desktop;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * JTable 控件的功能类。
 */

public class JTableUtils {


    /**
     * 定制列宽
     * @param table
     * @param tablePreferredWidth
     * @param percentages
     */
    public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {

            total += percentages[i];
        }
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }


    }

}