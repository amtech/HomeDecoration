package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Product;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;

/**
 * ��Ʒ�ı��ģ����
 */

public class ProductTableModel extends BaseTableModel<Product> {
    public String[] column=new String[]{"ͼƬ","����","���","��λ","���","��װ"};
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
