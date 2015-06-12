package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 产品表格模型
 */

public class MaterialTableModel extends BaseTableModel<Material> {
    public static String[] columnNames = new String[]{                         "图片",    "物料代码", "材料名称", "毛长", "毛宽", "毛高", "利用率","损耗率", "单价", "类型", "缓冲", "规格", "单位","类别","备注"};
    public static int[] columnWidth=new int[]{ ImageUtils.MAX_MATERIAL_MINIATURE_WIDTH,     100,        120,        40,     40,    40,     60,     60   ,   60,     40,    50,      120,    40,    60,ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{"photo","code", "name", "wLong", "wWidth", "wHeight", "available","discount", "price", "typeId", "classId", "spec","unitName","className","备注"};

    public  static Class[] classes = new Class[]{ImageIcon.class,Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public MaterialTableModel() {
        super(columnNames,fieldName,classes,Material.class);
    }




    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }


}
