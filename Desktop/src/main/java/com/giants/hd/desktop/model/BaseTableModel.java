package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.interf.Valuable;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 表格模型基类
 */

public  abstract class BaseTableModel<T> extends AbstractTableModel {


    public String[] columnNames  ;
    public String[] fieldName  ;
    public Field[] fields;
    public Class[] classes ;
    List<T> datas;
    public Class<T> itemClass;




    protected     int    MiniRowCount = 20;

    protected boolean editable=true;
    private boolean adjustable=true;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }



    public BaseTableModel( String[] columnNames, String[] fieldName,Class[] classes ,Class<T> itemClass)
    {

        this(new ArrayList<T>(),columnNames,fieldName,classes,itemClass);


    }


    public BaseTableModel(List<T> datas, String[] columnNames, String[] fieldName,Class[] classes ,Class<T> itemClass)
    {
        this.datas=datas;

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

                Logger.getLogger("TEST").info(fieldName[i] +" is not a field of class :"+itemClass);

            }

        }

        adjustRowCount();

    }

    /**
     * 调整默认显示的记录数
     */
    protected   void adjustRowCount()
    {

        if(!adjustable)return;

        int currentSize=this.datas.size();

        if(currentSize< MiniRowCount)
        {
            for(int i=currentSize;i< MiniRowCount;i++)

                addNewRow(i);
//                try {
//                    this.datas.add(itemClass.newInstance());
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }

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


    public List<T> getValuableDatas()
    {
        ArrayList<T> newArrays=new ArrayList<>();
        for(T data:datas)
        {
            if(data instanceof Valuable)
            {
                if(!((Valuable)data).isEmpty())
                {
                    newArrays.add(data);
                }
            }

        }

        return newArrays;

    }

    public void setDatas(List<T> newDatas) {



        this.datas.clear();

        if(newDatas!=null)
             this.datas.addAll(newDatas);
         adjustRowCount();

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


        if(fieldName[columnIndex].equals(ConstantData.COLUMN_INDEX))
        {
            return rowIndex+1;
        }

        if (fields[columnIndex] == null)
            return null;




        Object obj = null;
        try {
            obj= fields[columnIndex].get(getItem(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        //数字为0  显示空字符串
        if(  obj instanceof Number )
        {
            if( Math.abs(((Number) obj).floatValue())<=0.00001)
            return "";
            else
                if(obj instanceof  Float)
                {
                    //TODO   make the float value 3.0  to show in 3 ,without the fraction.
                    return FloatHelper.scale((Float)obj,5);
                }
        }else
        //显示图片
        if(obj instanceof  byte[] && getColumnClass(columnIndex).equals(ImageIcon.class))
        {

            return new ImageIcon((byte[])obj);

        }

        return obj
        ;


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
            datas.add(index,newItem);
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
     * 插入新数据
     * @param insertDatas
     * @param index
     */
    public void insertNewRows(List<T> insertDatas,int index)
    {
        datas.addAll(index,insertDatas);
        fireTableDataChanged();
    }


    /**
     * 删除多行
     * @param rowIndexs
     */
    public   void deleteRows(int[] rowIndexs)
    {



        List<T> datasToDelete=new ArrayList<>();
        for(int rowIndex:rowIndexs)
        {

            if(rowIndex>=0&&rowIndex<getRowCount())
            {
                datasToDelete.add(getItem(rowIndex));
            }
        }

        datas.removeAll(datasToDelete);

        fireTableDataChanged();

    }


    /**
     *定制列宽度
     * @return
     */
    public int[] getColumnWidth()
    {
        return null;
    }


    /**
     * 返回 设置行高
    * @return
     */
    public int getRowHeight() {

        return
                0;
    }


    /**
     * 获取文本为多行显示的列。默认为空。
      * @return
     */
    public int[] getMultiLineColumns()
    {
        return null;
    }


    /**
     * 是否自动追加记录
     * @param adjustable
     * @return
     */
    public void setRowAdjustable(boolean adjustable)
    {
        this.adjustable=adjustable;
        if(!adjustable)
        {
            datas.clear();

        }
    }
}