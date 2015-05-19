package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Product;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-05-19.
 */

public  abstract class BaseTableModel<T> extends AbstractTableModel {



    List<T> datas;

    public BaseTableModel( )
    {
        this(new ArrayList<T>());

    }

    public BaseTableModel(List<T> datas)
    {

        super();
       this.datas=datas;



    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas.clear();

        this.datas.addAll(datas);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }



    public T getItem(int rowIndex)
    {
        return datas.get(rowIndex);
    }



}
