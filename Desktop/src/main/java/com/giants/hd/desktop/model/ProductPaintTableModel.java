package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.ProductPaint;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends  BaseTableModel<ProductPaint>{
    public static String[] columnNames = new String[]{  "工序代码" , "工序名称" , "工价" , "材料名称" , "配料比例" , "单位" , "用量" , "物料单价" , "物料费用" , "稀释剂费用" };
    public static String[] fieldName = new String[]{"processCode", "processName", "processPrice", "materialName", "ingredientRatio", "unitName","materialQuantity", "materialPrice","materialCost","ingredientCost"};
    public  static Class[] classes = new Class[]{ };
    @Inject
    public ProductPaintTableModel() {
        super(columnNames,fieldName,classes,ProductPaint.class);
    }
}
