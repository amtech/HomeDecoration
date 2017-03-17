package com.giants3.hd.utils.logic;

import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.ProductDetail;

/**
 * 产品分析表 业务逻辑
 * Created by davidleen29 on 2017/3/15.
 */
public class ProductLogic {


    /**
     * 更新全部的统计数据        各个材料，工资，费用 金额汇总。
     * @param  productDetail
     * @param  globalData    相关参数
     */
    public  static void  updateStatistics(ProductDetail productDetail, GlobalData globalData)
    {}

    /**
     * 更新产品的统计数据
     */
    public static void updateProductStatistics(ProductDetail productDetail,GlobalData globalData)
    {




        float paintWage = 0;
        float paintCost = 0;
        for (ProductPaint paint : productDetail.paints) {
            paintWage += paint.processPrice;
            paintCost += paint.cost  ;

        }
        productDetail.product.updatePaintData(paintCost, paintWage,
            globalData);


        //汇总计算白胚材料
        float conceptusCost = 0;
        for (ProductMaterial material : productDetail.conceptusMaterials) {
            conceptusCost += material.getAmount();
        }
        productDetail.product.conceptusCost = FloatHelper.scale(conceptusCost);
        //汇总计算组白胚工资
        float conceptusWage = 0;
        for (ProductWage wage : productDetail.conceptusWages) {
            conceptusWage += wage.getAmount();
        }
        productDetail.product.conceptusWage = FloatHelper.scale(conceptusWage);


        //汇总计算组装材料
        float assembleCost = 0;
        for (ProductMaterial material : productDetail.assembleMaterials) {
            assembleCost += material.getAmount();
        }
        productDetail. product.assembleCost = FloatHelper.scale(assembleCost);

        //汇总计算组装工资
        float assembleWage = 0;
        for (ProductWage wage : productDetail.assembleWages) {
            assembleWage += wage.getAmount();
        }
        productDetail.product.assembleWage = FloatHelper.scale(assembleWage);

        //汇总计算包装材料
        float packCost = 0;
        for (ProductMaterial material : productDetail.packMaterials) {





            packCost += material.getAmount();


            //如果包材是外箱  则更新产品外包装材料信息


            if(material.getPackMaterialClass()!=null)
            {

                String className=material.getPackMaterialClass().getName();
                if(className.equals(PackMaterialClass.CLASS_BOX))
                {

                    productDetail.product.packLong=material.getpLong();
                    productDetail.product.packHeight=material.getpHeight();
                    productDetail.product.packWidth=material.getpWidth();

                }
                //取消数据联动
//                else if(PackMaterialClass.CLASS_INSIDE_BOX.equals(className))
//                {
//                    product.insideBoxQuantity=(int)material.quantity;
//                }
            }




        }




        //计算包装成本  平摊箱数， 如无箱数 则默认1
        productDetail.product.packCost = FloatHelper.scale(productDetail.product.packQuantity==0?packCost:packCost/productDetail.product.packQuantity);

        //汇总计算包装工资
        float packWage = 0;
        for (ProductWage wage : productDetail.packWages) {
            packWage += wage.getAmount();
        }
        //计算包装工资 平摊箱数， 如无箱数 则默认1
        productDetail.product.packWage = FloatHelper.scale(productDetail.product.packQuantity==0?packWage:packWage/productDetail.product.packQuantity);





        productDetail.summariables.clear();

        productDetail.summariables.addAll(productDetail.conceptusMaterials);
        productDetail.summariables.addAll(productDetail.assembleMaterials);
        productDetail.summariables.addAll(productDetail.paints);
        productDetail.summariables.addAll(productDetail.packMaterials);

        //分类型统计数据
        float cost1=0;
        float cost7=0;
        float cost8=0;
        float cost5=0;
        float cost6=0;
        float cost4=0;
        float cost11_15=0;
        for(Summariable summariable:productDetail.summariables)
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
        productDetail.summariables.clear();

        productDetail. product.cost1=FloatHelper.scale(cost1);
        productDetail. product.cost5=FloatHelper.scale(cost5);
        productDetail. product.cost6=FloatHelper.scale(cost6);
        productDetail. product.cost7=FloatHelper.scale(cost7);
        productDetail. product.cost8=FloatHelper.scale(cost8);
        productDetail. product.cost11_15=FloatHelper.scale(cost11_15);

        //计算包装材料 平摊箱数， 如无箱数 则默认1
        productDetail. product.cost4=FloatHelper.scale(productDetail.product.packQuantity==0?cost4:cost4/productDetail.product.packQuantity);









        /**
         * 重新计算总成本
         */
        productDetail.product.calculateTotalCost(globalData);




    }


    /**
     * 更新公式，启动调整
     * * @param  productDetail
     * @param  globalData    相关参数
     */
    public  void updateProductMaterialEquation(ProductDetail productDetail, GlobalData globalData)
    {








    }


    /**
     * 包装材料信息变动 --更新包装材料金额-- 更新统计数据
     * @param productDetail
     */
    public  void doOnProductPackMaterial(ProductDetail productDetail)
    {


        if(ArrayUtils.isEmpty(productDetail.packMaterials)) return ;



        for(ProductMaterial productMaterial:productDetail.packMaterials)
        {




        }



        //更新全部统计数据
        //updateStatistics();

    }


}
