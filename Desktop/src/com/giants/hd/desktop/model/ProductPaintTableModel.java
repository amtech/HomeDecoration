package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends BaseTableModel<ProductPaint> implements Materialable {
    public static String[] columnNames = new String[]{"工序代码", "工序名称", "工价","材料编码" ,"材料名称", "配料比例", "单位", "用量", "物料单价", "物料费用", "稀释费用","备注"};

    public static int[] columnWidths = new int []{      80,             80,    60,     100,        120,       80,        40,     60,       60,      60,         80,ConstantData.MAX_COLUMN_WIDTH };



    public static String[] fieldName = new String[]{"processCode", "processName", "processPrice","materialCode", "materialName", "ingredientRatio", "unitName", "materialQuantity", "materialPrice", "materialCost", "ingredientCost","memo"};
    public static Class[] classes = new Class[]{String.class, String.class, Object.class,  Material.class,Material.class, Object.class, String.class };

    public static boolean[] editables = new boolean[]{true, true, true, true,true,  true, false, true, false, false, false, true };

    @Inject
    public ProductPaintTableModel() {
        super(columnNames, fieldName, classes, ProductPaint.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if("洗枪洗笔刷费用".equals(getItem(rowIndex).processName))
        {
            return false;
        }
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
            case 5:
                item.setIngredientRatio(Float.valueOf(aValue.toString()));
                item.updateMaterialAndIngredientCost();
                break;

            //"用量"
            case 7:
                item.setMaterialQuantity(Float.valueOf(aValue.toString()));
                item.updateMaterialAndIngredientCost();
                break;

            //"备注说明"
            case 11:
                item.setMemo(aValue.toString());
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



    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }

}
