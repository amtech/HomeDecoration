package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.ConfigData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.entity.ProductProcess;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends BaseTableModel<ProductPaint> implements Materialable {



    public static final String COLUMN_materialQuantity="materialQuantity";
    public static final String COLUMN_ingredientRatio="ingredientRatio";
    public static String[] columnNames = new String[]{"序号","工序代码", "工序名称", "工价","材料编码" ,"材料名称", "配料比例", "单位", "用量", "物料单价", "物料费用", "稀释费用","备注"};

    public static int[] columnWidths = new int []{      40,   80,             80,    60,     100,        120,       80,        40,     60,       60,      60,         80,ConstantData.MAX_COLUMN_WIDTH };



    public static String[] fieldName = new String[]{ ConstantData.COLUMN_INDEX,"processCode", "processName", "processPrice","materialCode", "materialName", "ingredientRatio", "unitName", "materialQuantity", "materialPrice", "materialCost", "ingredientCost","memo"};
    public static Class[] classes = new Class[]{Object.class,String.class, String.class, Object.class,  Material.class,Material.class, Object.class, String.class };

    public static boolean[] editables = new boolean[]{false,true, true, true, true,true,  true, false, true, false, false, false, true };

    @Inject
    public ProductPaintTableModel() {
        super(columnNames, fieldName, classes, ProductPaint.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        //当前行是XISHUA 行  并且不是材料选择行
        if(ProductProcess.XISHUA.equals(getItem(rowIndex).processName)&&(columnIndex==6||columnIndex==8))
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
            case 1:
                item.setProcessCode(aValue.toString());
                break;
            //"工序名称"
            case 2:
                item.setProcessName(aValue.toString());
                /**
                 * 当前行修改是洗刷统计行
                 */
                if(ProductProcess.XISHUA.equals(item.processName))
                {
                    updateQuantityOfIngredient();

                }


                break;
                //"工价"
            case 3:
                item.setProcessPrice(Float.valueOf(aValue.toString()));
                break;
            //"配料比例"
            case 6:
                item.setIngredientRatio(Float.valueOf(aValue.toString()));
                item.updateMaterialAndIngredientCost();
                updateQuantityOfIngredient();
                break;

            //"用量"
            case 8:
                item.setMaterialQuantity(Float.valueOf(aValue.toString()));

                item.updateMaterialAndIngredientCost();
                updateQuantityOfIngredient();
                break;

            //"备注说明"
            case 12:
                item.setMemo(aValue.toString());
                break;

        }


        fireTableRowsUpdated(rowIndex, rowIndex);




    }

    /**
     * 更新配料洗刷枪的费用的数据量值。
     */
    public void updateQuantityOfIngredient()
    {


        ProductPaint ingredientPaint=null;
        float totalIngredientQuantity=0;
        for(ProductPaint paint:datas)
        {

            if(!ProductProcess.XISHUA.equals(paint.processName))
            {
                if(paint.materialId>0)
                totalIngredientQuantity+=paint.ingredientQuantity;
            }else
                ingredientPaint=paint;

        }

        if(ingredientPaint!=null)
        {


            ingredientPaint.setMaterialQuantity(FloatHelper.scale(totalIngredientQuantity* ConfigData.getInstance().extra_ratio_of_diluent,3));
            int index=datas.indexOf(ingredientPaint);

            fireTableRowsUpdated(index, index);
        }


    }


    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductPaint productPaint=getItem(rowIndex);
        if(productPaint!=null)
        {
            productPaint.updateMaterial(material);
        }

        if(ProductProcess.XISHUA.equals(productPaint.processName))
        {


            for(ProductPaint paint:datas)
            {
                //如果配料单价有改变  则适配新单价
                if(Float.compare(paint.price_of_diluent, material.price) != 0) {

                    paint.setIngredientMaterial(
                            material
                    );
                    int index=datas.indexOf(paint);
                    fireTableRowsUpdated(index,index);
                }

            }


        }else
        {
            //找到配料行
            ProductPaint foundXishua=null;
            for(ProductPaint paint:datas)
            {

                if(ProductProcess.XISHUA.equals(paint.processName))
                {
                    foundXishua=paint;

                    break;

                }


            }

            if(foundXishua!=null)
            for(ProductPaint paint:datas)
            {

                //如果配料单价有改变  则适配新单价
                if(Float.compare(paint.price_of_diluent, foundXishua.price_of_diluent) != 0) {

                    paint.price_of_diluent=foundXishua.price_of_diluent;
                    paint.updateMaterialAndIngredientCost();
                    int index=datas.indexOf(paint);
                    fireTableRowsUpdated(index,index);
                }


            }



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
