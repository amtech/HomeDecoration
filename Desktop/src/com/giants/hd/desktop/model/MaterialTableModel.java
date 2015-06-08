package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.Material;
import com.google.inject.Inject;

/**
 * 产品表格模型
 */

public class MaterialTableModel extends BaseTableModel<Material> {
    public static String[] columnNames = new String[]{"物料代码", "材料名称", "毛长", "毛宽", "毛高", "利用率","损耗率", "单价", "类型", "缓冲", "规格", "单位","类别"};
    public static String[] fieldName = new String[]{"code", "name", "wLong", "wWidth", "wHeight", "available","discount", "price", "typeId", "classId", "spec","unitName","className"};

    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public MaterialTableModel() {
        super(columnNames,fieldName,classes,Material.class);
    }






}
