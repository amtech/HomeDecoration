package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Product;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-05-19.
 */

public  abstract class BaseTableModel<T> extends AbstractTableModel {


    public String[] columnNames  ;
    public String[] fieldName  ;
    public Field[] fields;
    public Class[] classes ;
    List<T> datas;
    public Class<T> itemClass;





    public BaseTableModel( String[] columnNames, String[] fieldName,Class[] classes ,Class<T> itemClass)
    {
        this.datas=new ArrayList<T>();

        this.classes=classes;
        this.fieldName=fieldName;
        this.columnNames=columnNames;
        this.itemClass=itemClass;
        int size = fieldName.length;
        fields = new Field[size];

        for (int i = 0; i < size; i++) {

            try {
                fields[i] = itemClass.getField(fieldName[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


        }


    }



    @Override
    public int getColumnCount() {
        return columnNames.length;
    }


    @Override
    public Class getColumnClass(int c) {


        if(c>=0&&c<classes.length)

        return classes[c];
        else
            return Object.class;

    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {



        this.datas.clear();

        if(datas!=null)
             this.datas.addAll(datas);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }



    public T getItem(int rowIndex)
    {
        if(rowIndex<0||rowIndex>=datas.size()) return null;
        return datas.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (fields[columnIndex] == null) return null;

        try {
            return fields[columnIndex].get(getItem(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**\
     * 添加新行
     * @param index
     */
    public  T addNewRow(int index)   {

        T newItem= null;
        try {
            newItem = itemClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(null==newItem) return null;
        if(index<0||index>=getRowCount())
        {
            datas.add(newItem);
        }
        else
            datas.add(index+1,newItem);
        fireTableDataChanged();
        return newItem;

    }

    /**
     * 删除行
     * @param rowIndex
     */
    public   void deleteRow(int rowIndex)
    {
        if(rowIndex>=0&&rowIndex<getRowCount())
        {


            datas.remove(rowIndex);
            fireTableDataChanged();
        }


    }


    /**
     *定制列宽度
     * @return
     */
    public float[] getColumnWidth()
    {
        return null;
    }

}
