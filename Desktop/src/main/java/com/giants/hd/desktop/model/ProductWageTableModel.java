package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.ProductWage;
import com.google.inject.Inject;

/**
 * 产品工资列表模型
 */

public class ProductWageTableModel extends  BaseTableModel<ProductWage> {

    public static String[] columnNames = new String[]{"工序编码", "工序名称", " 工价 ", " 金额 ","备注                    "};
    public static String[] fieldName = new String[]{"processCode", "processName", "price", "amount", "memo" };
    public  static Class[] classes = new Class[]{String.class, String.class, Float.class, Float.class, String.class };

    public  static boolean[] editables = new boolean[]{true, true, true, true, true};

    @Inject
    public ProductWageTableModel() {
        super(columnNames,fieldName,classes,ProductWage.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductWage data=getItem(rowIndex);
        switch (columnIndex)
        {

            case 0:
                 data.setProcessCode(aValue.toString());

                break;
            case 1:

                data.setProcessName(aValue.toString());
                break;


            case 2:

                data.setPrice(Float.valueOf(aValue.toString()));
                data.setAmount(data.price);
                break;


            case 4:

               data.setMemo(aValue.toString());
                break;


        }


        fireTableRowsUpdated(rowIndex,rowIndex);


    }



}
