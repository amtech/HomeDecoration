package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.utils.RandomUtils;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.entity.GlobalData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.entity.ProductProcess;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.ProductPaintArrayList;
import com.google.inject.Inject;

import java.util.List;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends BaseTableModel<ProductPaint> implements Materialable {

    GlobalData globalData= CacheManager.getInstance().bufferData.globalData;

    public static final String COLUMN_materialQuantity="materialQuantity";
    public static final String COLUMN_ingredientRatio="ingredientRatio";
    public static String[] columnNames = new String[]{"序号","工序代码", "工序名称", "工价","材料编码" ,"材料名称", "配料比例", "单位", "用量", "物料单价", "物料费用", "配料量","稀释剂","备注"};

    public static int[] columnWidths = new int []{      40,   80,             80,    60,     100,        120,       80,        40,     60,       60,      60,          60,        60, ConstantData.MAX_COLUMN_WIDTH };



    public static String[] fieldName = new String[]{ ConstantData.COLUMN_INDEX,"processCode", "processName", "processPrice","materialCode", "materialName", "ingredientRatio", "unitName", "quantity", "price", "cost", "materialQuantity","ingredientQuantity","memo"};
    public static Class[] classes = new Class[]{Object.class,String.class, String.class, Object.class,  Material.class,Material.class, Object.class, String.class };

    public static boolean[] editables = new boolean[]{false,true, true, true, true,true,  true, false, true, false, false, false,false, true };


    @Inject
    public ProductPaintTableModel() {
        super(new ProductPaintArrayList(),columnNames, fieldName, classes, ProductPaint.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        //当前行是XISHUA 行  并且不是材料选择行
        String processName=getItem(rowIndex).processName;
        if(processName!=null&&processName.contains(ProductProcess.XISHUA)&&(columnIndex==6||columnIndex==8))
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
                item.setProcessName(aValue.toString().trim());
                /**
                 * 当前行修改是洗刷统计行
                 */
                if(item.processName!=null&&item.processName.contains(ProductProcess.XISHUA) )
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
                item.ingredientRatio=Float.valueOf(aValue.toString());
                item.updatePriceAndCostAndQuantity(globalData);
                updateQuantityOfIngredient();
                break;

            //"用量"
            case 8:

                item.quantity=  Float.valueOf(aValue.toString());

                item.updatePriceAndCostAndQuantity(globalData);
                updateQuantityOfIngredient();
                break;

            //"备注说明"
            case 13:
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



        if(datas instanceof  ProductPaintArrayList)
        {

            ProductPaintArrayList list=(ProductPaintArrayList)datas;
           int index= list.updateQuantityOfIngredient(globalData);
            if(index>-1)
                fireTableRowsUpdated(index, index);
        }



    }


    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductPaint productPaint=getItem(rowIndex);
        if(productPaint!=null)
        {
            productPaint.updateMaterial(material,globalData);
        }


//        if(ProductProcess.XISHUA.equals(productPaint.processName))
//
//        {
//
//            //找出其他材料的稀释剂用量
//
//            float totalGrient=0;
//            for(ProductPaint temp:datas)
//            {
//                totalGrient+=temp.ingredientQuantity;
//            }
//
//            productPaint.quantity=totalGrient*0.1f;
//
//            productPaint.updatePriceAndCostAndQuantity();
//
//
//
//
//        }



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


    @Override
    public ProductPaint addNewRow(int index) {
        ProductPaint productPaint= super.addNewRow(index);

        productPaint.id= -RandomUtils.nextInt();
        return productPaint;
    }


    @Override
    public void insertNewRows(List<ProductPaint> insertDatas, int index) {

        for (ProductPaint paint:insertDatas)
        {
            paint.id=-1;
        }
        super.insertNewRows(insertDatas, index);
    }
}
