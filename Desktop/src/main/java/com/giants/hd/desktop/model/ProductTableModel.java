package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Product;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;

/**
 * 产品的表格模型类
 */

public class ProductTableModel extends BaseTableModel<Product> {
    public String[] column=new String[]{"图片","货号","规格","单位","类别","包装"};
    public String[] fieldName=new String[]{"photo","name","","pTypeName","pClassName",""};
    Field[] fields;

    public ProductTableModel( ) {
        super();


        fields=Product.class.getFields();

    }

    @Override
    public int getColumnCount() {
        return column.length;
    }





    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Product product=getItem(rowIndex);

        for(String field:fieldName)
        {

//            if(fields[])

        }



        return null;
    }
}
