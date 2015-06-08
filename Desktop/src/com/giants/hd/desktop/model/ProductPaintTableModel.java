package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductPaint;
import com.google.inject.Inject;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends BaseTableModel<ProductPaint> implements Materialable {
    public static String[] columnNames = new String[]{"工序代码", "工序名称", "工价", "材料名称", "配料比例", "单位", "用量", "物料单价", "物料费用", "稀释剂费用"};
    public static String[] fieldName = new String[]{"processCode", "processName", "processPrice", "materialName", "ingredientRatio", "unitName", "materialQuantity", "materialPrice", "materialCost", "ingredientCost"};
    public static Class[] classes = new Class[]{String.class, String.class, Float.class, Material.class, Float.class, String.class, Float.class, Float.class, Float.class, Float.class};

    public static boolean[] editables = new boolean[]{true, true, true, true, true, false, true, false, false, false, false, true, false, false, true, true};

    @Inject
    public ProductPaintTableModel() {
        super(columnNames, fieldName, classes, ProductPaint.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductPaint item = getItem(rowIndex);
        switch (columnIndex) {
            // "工序代码"
            case 0:
                item.setProcessCode(aValue.toString());
                break;
            //"工序名称"
            case 1:
                item.setProcessName(aValue.toString());
                break;
                //"工价"
            case 2:
                item.setProcessPrice(Float.valueOf(aValue.toString()));
                break;
            //"配料比例"
            case 4:
                item.setIngredientRatio(Float.valueOf(aValue.toString()));
                item.updateMaterialAndIngredientCost();
                break;

            //"用量"
            case 6:
                item.setMaterialQuantity(Float.valueOf(aValue.toString()));
                item.updateMaterialAndIngredientCost();
                break;

        }

        fireTableRowsUpdated(rowIndex, rowIndex);


    }


    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductPaint productPaint=getItem(rowIndex);
        if(productPaint!=null)
        {
            productPaint.updateMaterial(material);
        }


        fireTableRowsUpdated(rowIndex,rowIndex);

    }
}
