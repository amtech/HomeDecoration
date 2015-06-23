package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 材料列表  表格模型
 */

public class ProductMaterialTableModel extends  BaseTableModel<ProductMaterial> implements Materialable{

    public static String[] columnNames = new String[]{"序号","物料编码", "材料名称", "数量","长","宽","高","毛长", "毛宽", "毛高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static int[] columnWidths = new int []{     40,    100,        120,        40,   40,  40, 40,  40,    40,  40,   80,    40,    60,     40,     60,   80, ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"materialCode", "materialName", "quantity", "pLong", "pWidth", "pHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price","amount","memo"};
    public  static Class[] classes = new Class[]{Object.class,Material.class, Material.class};

    public  static boolean[] editables = new boolean[]{false,true, true, true, true, true, true,false,false,false , false, false, true, false,false,true,true };

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
            case 3:
                //设置用量
                material.setQuantity(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 4:
                //设置长
            {
                String valueString = aValue.toString().trim();
                float value = StringUtils.isEmpty(valueString) ? 0 : Float.valueOf(valueString);

                material.setpLong(value);
                material.update();
            }
                break;


            case 5:
                //设置宽
            {
                String valueString = aValue.toString().trim();
                float value = StringUtils.isEmpty(valueString) ? 0 : Float.valueOf(valueString);
                material.setpWidth(value);
                material.update();
            }
                break;

            case 6:
                //设置高
            {
                String valueString = aValue.toString().trim();
                float value = StringUtils.isEmpty(valueString) ? 0 : Float.valueOf(valueString);
                material.setpWidth(value);
                material.setpHeight(value);
                material.update();
            }
                break;

            case 12:
                //设置利用率
                material.setAvailable(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 16:
                //设置备注
                material.setMemo( aValue.toString());
                break;



        }


        fireTableRowsUpdated(rowIndex,rowIndex);


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
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }
}
