package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.FloatHelper;

import java.util.ArrayList;

/**
 *  油漆集合数据类型  提供关联数据的调整
 */
public class ProductPaintArrayList extends ArrayList<ProductPaint> {



    /**
     * 更新配料洗刷枪的费用的数据量值。
     *
     * @return  更新记录所在位置index'
     */
    public int updateQuantityOfIngredient()
    {


        ProductPaint ingredientPaint=null;
        float totalIngredientQuantity=0;
        for(ProductPaint paint:this)
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


            ingredientPaint.quantity=FloatHelper.scale(totalIngredientQuantity * ConfigData.getInstance().extra_ratio_of_diluent, 3);
            ingredientPaint.updatePriceAndCostAndQuantity();
            int index=this.indexOf(ingredientPaint);
            return index;
            //fireTableRowsUpdated(index, index);
        }


        return -1;


    }

}
