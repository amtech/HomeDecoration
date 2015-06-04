package com.giants.hd.desktop.model;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.*;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 包装 输入表格 模型
 */

public class ProductPackMaterialTableModel extends  BaseTableModel<ProductMaterial> implements Materialable{

    public static String[] columnNames = new String[]{"  材料类别    ","  材质     ","  位置    ","  物料编码   ", "材料名称", "数量","长","宽","高","长", "宽", "高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static String[] fieldName = new String[]{"packMaterialClass","packMaterialType","packMaterialPosition","materialCode", "materialName", "quantity", "pLong", "pWidth", "pHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price","amount","memo"};
    public  static Class[] classes = new Class[]{PackMaterialClass.class,PackMaterialType.class,PackMaterialPosition.class,Material.class, Material.class, Float.class, Float.class, Float.class, Float.class};

    public  static boolean[] editables = new boolean[]{true, true, true,true, true, true, true, true, true,false,false,false , false, false, true, false,false,true,true };

    private Product product;


    @Inject
    public ProductPackMaterialTableModel() {
        super(columnNames,fieldName,classes,ProductMaterial.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value= super.getValueAt(rowIndex, columnIndex);

        if(fieldName[columnIndex].equals("amount")&&value instanceof Float&&product!=null&&product.packQuantity!=0)
        {

            float floatValue=Float.valueOf(value.toString());
            value= FloatHelper.scale(floatValue/product.packQuantity);

        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductMaterial material=getItem(rowIndex);
        switch (columnIndex)
        {


            case 0:
                //设置包装材料大类型
                material.setPackMaterialClass((PackMaterialClass) aValue);


                break;


            case 1:
                //设置材料类型
                material.setPackMaterialType((PackMaterialType) aValue);


                break;


            case 2:
                //设置使用位置
                material.setPackMaterialPosition((PackMaterialPosition) aValue);

                break;
            case 5:
                //设置用量
                material.setQuantity(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 6:
                //设置长
                material.setpLong(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 7:
                //设置宽
                material.setpWidth(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 8:
                //设置高
                material.setpHeight(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 14:
                //设置高
                material.setAvailable(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 18:
                //设置
                material.setMemo( aValue.toString());
                break;



        }


        fireTableRowsUpdated(rowIndex,rowIndex);


    }


    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductMaterial productMaterial=getItem(rowIndex);
        if(productMaterial!=null)
        {
            productMaterial.updateMaterial(material);
        }


        fireTableRowsUpdated(rowIndex,rowIndex);

    }


    @Override
    public ProductMaterial addNewRow(int index) {
        ProductMaterial p=     super.addNewRow(index);

        //包装的计算公式不一致  需要在本地标记类型id
        p.setFlowId(Flow.FLOW_PACK);
        return p;
    }
}
