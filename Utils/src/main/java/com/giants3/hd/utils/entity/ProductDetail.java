package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.FloatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品详细信息
 */
public class ProductDetail   {


    public Product product;
    /**
     * 白胚材料列表
     */
    public List<ProductMaterial> conceptusMaterials;
    /**
     * 组装材料列表
     */
    public List<ProductMaterial> assembleMaterials;

    /**
     * 油漆材料工序列表
     */
    public  List<ProductPaint> paints;


    /**
     * 白胚工资列表
     */

    public List<ProductWage> conceptusWages;
    /**
     * 组装工资列表
     */
    public List<ProductWage> assembleWages;
    /**
     *
     *
     * 包装材料列表
     */
    public List<ProductMaterial> packMaterials;
    /**
     * 包装工资列表
     */
    public List<ProductWage> packWages;


    /**
     * 更新产品的统计数据
     */
    public  void updateProductStatistics()
    {




        float paintWage = 0;
        float paintCost = 0;
        for (ProductPaint paint : paints) {
            paintWage += paint.processPrice;
            paintCost += paint.materialCost + paint.ingredientCost;









        }
        product.updatePaintData(paintCost, paintWage);


        //汇总计算白胚材料
        float conceptusCost = 0;
        for (ProductMaterial material : conceptusMaterials) {
            conceptusCost += material.getAmount();
        }
        product.conceptusCost = FloatHelper.scale(conceptusCost);
        //汇总计算组白胚工资
        float conceptusWage = 0;
        for (ProductWage wage : conceptusWages) {
            conceptusWage += wage.getAmount();
        }
        product.conceptusWage = FloatHelper.scale(conceptusWage);


        //汇总计算组装材料
        float assembleCost = 0;
        for (ProductMaterial material : assembleMaterials) {
            assembleCost += material.getAmount();
        }
        product.assembleCost = FloatHelper.scale(assembleCost);

        //汇总计算组装工资
        float assembleWage = 0;
        for (ProductWage wage : assembleWages) {
            assembleWage += wage.getAmount();
        }
        product.assembleWage = FloatHelper.scale(assembleWage);

        //汇总计算包装材料
        float packCost = 0;
        for (ProductMaterial material : packMaterials) {





            packCost += material.getAmount();


            //如果包材是外箱  则更新产品外包装材料信息


            if(material.getPackMaterialClass()!=null)
            {

                String className=material.getPackMaterialClass().getName();
                if(className.equals(PackMaterialClass.CLASS_BOX))
                {

                    product.packLong=material.getpLong();
                    product.packHeight=material.getpHeight();
                    product.packWidth=material.getpWidth();

                }else if(PackMaterialClass.CLASS_INSIDE_BOX.equals(className))
                {
                    product.insideBoxQuantity=(int)material.quantity;
                }
            }






        }




        //计算包装成本  平摊箱数， 如无箱数 则默认1
        product.packCost = FloatHelper.scale(product.packQuantity==0?packCost:packCost/product.packQuantity);

        //汇总计算包装工资
        float packWage = 0;
        for (ProductWage wage : packWages) {
            packWage += wage.getAmount();
        }
        //计算包装工资 平摊箱数， 如无箱数 则默认1
        product.packWage = FloatHelper.scale(product.packQuantity==0?packWage:packWage/product.packQuantity);





        summariables.clear();

        summariables.addAll(conceptusMaterials);
        summariables.addAll(assembleMaterials);
        summariables.addAll(paints);
        summariables.addAll(packMaterials);

        //分类型统计数据
        float cost1=0;
        float cost7=0;
        float cost8=0;
        float cost5=0;
        float cost6=0;
        float cost4=0;
        float cost11_15=0;
        for(Summariable summariable:summariables)
        {
            int type=summariable.getType();
            float amount=summariable.getAmount();
            switch (type)
            {

                case 1:
                    cost1+=amount;
                            break;
                case 8:
                    cost8+=amount;
                    break;
                case 5:
                    cost5+=amount;
                    break;
                case 6:
                    cost6+=amount;
                    break;
                case 7:
                    cost7+=amount;
                    break;

                case 4:
                    cost4+=amount;
                    break;
                case 11:case 12: case 13:case 14:case 15:
                    cost11_15+=amount;
                    break;


            }

        }
        summariables.clear();

        product.cost1=FloatHelper.scale(cost1);
        product.cost5=FloatHelper.scale(cost5);
        product.cost6=FloatHelper.scale(cost6);
        product.cost7=FloatHelper.scale(cost7);
        product.cost8=FloatHelper.scale(cost8);
        product.cost11_15=FloatHelper.scale(cost11_15);

        //计算包装材料 平摊箱数， 如无箱数 则默认1
        product.cost4=FloatHelper.scale(product.packQuantity==0?cost4:cost4/product.packQuantity);









        /**
         * 重新计算总成本
         */
        product.calculateTotalCost();




    }


    public List<Summariable> summariables=new ArrayList<>();



}
