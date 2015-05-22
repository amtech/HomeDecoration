package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 材料列表  表格模型
 */

public class ProductMaterialTableModel extends  BaseTableModel<ProductMaterial>{

    public static String[] columnNames = new String[]{"物料编码", "材料名称", "数量","长","宽","高","长", "宽", "高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static String[] fieldName = new String[]{"materialCode", "materialName", "quantity", "mLong", "mWidth", "mHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price","amount","memo"};
    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};
    @Inject
    public ProductMaterialTableModel() {
        super(columnNames,fieldName,classes,ProductMaterial.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);
    }



    public void  setMaterial(Material material,int rowIndex)
    {


        ProductMaterial productMaterial=getItem(rowIndex);
        if(productMaterial!=null)
        {
            productMaterial.setMaterial(material);
        }


    }
}
