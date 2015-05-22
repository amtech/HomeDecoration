package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.google.inject.Inject;

/**
 * 材料列表  表格模型
 */

public class ProductMaterialTableModel extends  BaseTableModel<ProductMaterial>{

    public static String[] columnNames = new String[]{"物料编码", "材料名称", "数量","长","宽","高","长", "宽", "高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static String[] fieldName = new String[]{"materialCode", "materialName", "quantity", "mLong", "mWidth", "mHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price","amount","memo"};
    public  static Class[] classes = new Class[]{String.class, String.class, Float.class, Float.class, Float.class, Float.class};

    public  static boolean[] editables = new boolean[]{true, true, true, true, true, true,false,false,false , false, false, true, false,false,true,true };

    @Inject
    public ProductMaterialTableModel() {
        super(columnNames,fieldName,classes,ProductMaterial.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductMaterial material=getItem(rowIndex);
        switch (columnIndex)
        {
            case 2:
                material.setQuantity(Float.valueOf(aValue.toString()));
                material.setAmount(material.quantity*material.price);

                break;
        }


        fireTableRowsUpdated(rowIndex,rowIndex);


    }



    public void  setMaterial(Material material,int rowIndex)
    {


        ProductMaterial productMaterial=getItem(rowIndex);
        if(productMaterial!=null)
        {
            productMaterial.updateMaterial(material);
        }


        fireTableRowsUpdated(rowIndex,rowIndex);

    }
}
