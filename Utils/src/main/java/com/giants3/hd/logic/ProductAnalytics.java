package com.giants3.hd.logic;

import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.quotation.BoardQuotation;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by davidleen29 on 2018/4/24.
 */
public class ProductAnalytics {





    public static void updateProduct(ProductDetail productDetail, GlobalData globalData)
    {


        float paintWage = 0;
        float paintCost = 0;
        for (ProductPaint paint : productDetail.paints) {
            paintWage += paint.processPrice;
            paintCost += paint.cost  ;

        }
        final Product product = productDetail.product;
        updatePaintData(product, paintCost, paintWage,
                globalData);


        //汇总计算白胚材料
        float conceptusCost = 0;
        for (ProductMaterial material : productDetail.conceptusMaterials) {
            conceptusCost += material.getAmount();
        }
        product.conceptusCost = FloatHelper.scale(conceptusCost);
        //汇总计算组白胚工资
        float conceptusWage = 0;
        for (ProductWage wage : productDetail.conceptusWages) {
            conceptusWage += wage.getAmount();
        }
        product.conceptusWage = FloatHelper.scale(conceptusWage);


        //汇总计算组装材料
        float assembleCost = 0;
        for (ProductMaterial material : productDetail.assembleMaterials) {
            assembleCost += material.getAmount();
        }
        product.assembleCost = FloatHelper.scale(assembleCost);

        //汇总计算组装工资
        float assembleWage = 0;
        for (ProductWage wage : productDetail.assembleWages) {
            assembleWage += wage.getAmount();
        }
        product.assembleWage = FloatHelper.scale(assembleWage);

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

                    product.packLong=material.getpLong();
                    product.packHeight=material.getpHeight();
                    product.packWidth=material.getpWidth();

                }
                //取消数据联动
//                else if(PackMaterialClass.CLASS_INSIDE_BOX.equals(className))
//                {
//                    product.insideBoxQuantity=(int)material.quantity;
//                }
            }




        }




        //计算包装成本  平摊箱数， 如无箱数 则默认1
        product.packCost = FloatHelper.scale(product.packQuantity==0?packCost:packCost/ product.packQuantity);

        //汇总计算包装工资
        float packWage = 0;
        for (ProductWage wage : productDetail.packWages) {
            packWage += wage.getAmount();
        }
        //计算包装工资 平摊箱数， 如无箱数 则默认1
        product.packWage = FloatHelper.scale(product.packQuantity==0?packWage:packWage/ product.packQuantity);





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

        product.cost1=FloatHelper.scale(cost1);
        product.cost5=FloatHelper.scale(cost5);
        product.cost6=FloatHelper.scale(cost6);
        product.cost7=FloatHelper.scale(cost7);
        product.cost8=FloatHelper.scale(cost8);
        product.cost11_15=FloatHelper.scale(cost11_15);

        //计算包装材料 平摊箱数， 如无箱数 则默认1
        product.cost4=FloatHelper.scale(product.packQuantity==0?cost4:cost4/ product.packQuantity);
        updateProductInfoOnly(globalData, product);


    }
    /**
     * 重新计算总成本
     */
    public static void updateProductInfoOnly(GlobalData globalData, Product product) {


        //各道材料++修理工资+搬运工资
        product.productCost= FloatHelper.scale( product.paintCost+ product.paintWage+ product.assembleCost+ product.assembleWage+ product.conceptusCost+ product.conceptusWage+ product.packCost+ product.packWage+ product.repairCost+ product.banyunCost);


        //计算体积
        product.packVolume= FloatHelper.scale(product.packLong* product.packWidth* product.packHeight/1000000,3);


        //计算修理工资
        product.repairCost=FloatHelper.scale(product.packQuantity<=0?0: product.packVolume* globalData.repairPrice/ product.packQuantity);

        //计算搬运工资
        product.banyunCost=FloatHelper.scale(product.packQuantity<=0?0: product.packVolume* globalData.priceOfBanyun/ product.packQuantity);

        GlobalData configData= globalData;


        //获取管理系数  咸康跟普通不一致
        float manageRatio= globalData.manageRatioNormal;
        if(product.pack!=null&& product.pack.isXkPack())
        {
            manageRatio= globalData.manageRatioXK;
        }


        //计算成本价   (实际成本)*（1+管理系数）
        product.cost=FloatHelper.scale((product.productCost)*(1+manageRatio));


        product.price=FloatHelper.scale(product.cost/configData.cost_price_ratio);

        if(product.packQuantity <=0)
            product.fob=0;
        else
        //售价+海外运费
        product.fob=FloatHelper.scale((product.price * (1 + configData.addition) + product.packVolume * configData.price_of_export / product.packQuantity)/configData.exportRate);
    }

    /**
     * 更新材料费用与配料费用
     */
    public static void updateProductPaintPriceAndCostAndQuantity(ProductPaint productPaint,GlobalData globalData)
    {
        GlobalData configData=globalData;
        productPaint.price=FloatHelper.scale((productPaint.materialPrice + configData.price_of_diluent * productPaint.ingredientRatio)/(1+productPaint.ingredientRatio),3);
        productPaint.cost=FloatHelper.scale(productPaint.quantity*productPaint.price);

        //配料 即主成分用量
        productPaint.materialQuantity= FloatHelper.scale(productPaint.quantity/(1+productPaint.ingredientRatio),3) ;
        productPaint.ingredientQuantity=productPaint.quantity-productPaint.materialQuantity ;


    }

    /**
     * 更新材料数据
     * @param productPaint
     * @param material
     */
    public static void updateMaterial(ProductPaint productPaint, Material material, GlobalData globalData) {






        productPaint.materialCode=material.code;
        productPaint.materialName=material.name;
        productPaint.materialId=material.id;
        productPaint.materialPrice=material.price;


        productPaint.unitName=material.unitName;
        productPaint.materialType=material.typeId;
        productPaint.ingredientRatio=material.ingredientRatio;
         productPaint.memo=material.memo;


        updateProductPaintPriceAndCostAndQuantity(productPaint, globalData);


    }

    /**
     * 默认计算公式  长*宽*高*数量、利用率
     * @param item
     */
    public static float defaultCalculateQuota(ProductMaterial item) {

        float newQuota = 0;
        //默认计算公式
        if (item.pLong <= 0 && item.pWidth <= 0 && item.pHeight <= 0) {
            newQuota = item.available <= 0 ? 0 : item.quantity / item.available;

        } else if (item.pWidth <= 0 && item.pHeight <= 0) {
            newQuota = item.available <= 0 ? 0 : item.quantity * item.wLong / 100 / item.available;
        } else if (item.pHeight <= 0) {
            newQuota = item.available <= 0 ? 0 : item.quantity * item.wLong * item.wWidth / 10000 / item.available;
        } else {
            newQuota = item.available <= 0 ? 0 : item.quantity * item.wLong * item.wWidth * item.wHeight / 1000000 / item.available;
        }


        return newQuota;


    }

    /**
     * 更新材料相关数据
     * @param productMaterial
     */
    public static void update(ProductMaterial productMaterial) {


        productMaterial.wLong = productMaterial.mLong + productMaterial.pLong;
        productMaterial.wHeight = productMaterial.mHeight + productMaterial.pHeight;
        productMaterial.wWidth = productMaterial.mWidth + productMaterial.pWidth;


        //计算定额


        float newQuota = 0;


        if (productMaterial.materialId <= 0)
            newQuota = 0;
        else if (productMaterial.quantity <= 0)
            newQuota = 0;
        else


        {

            if (BoardQuotation.work(productMaterial)) {

                BoardQuotation.CutResult cutResult = BoardQuotation.update(productMaterial, null);
                if (cutResult != null) {
                    productMaterial.available = cutResult.available;
                    productMaterial.spec = cutResult.spec;
                    productMaterial.cutWay = cutResult.cutWay;
                }
            }

            //如果是包装材料  采用特殊计算方式
            if (productMaterial.flowId == Flow.FLOW_PACK && productMaterial.packMaterialClass != null) {


                switch (productMaterial.packMaterialClass.name) {

                    case PackMaterialClass.CLASS_BOX:
                        if (productMaterial.pWidth < 15) productMaterial.wWidth = productMaterial.pWidth * 2;
//					if( pLong+pWidth>130)
//					{
//						newQuota=(pLong/100+pWidth/100+0.17f)*(wWidth/100+pHeight/100+0.07f)*2*quantity;
//					}else
//					{
//						newQuota=(pLong/100+pWidth/100+0.09f)*(wWidth/100+pHeight/100+0.07f)*2*quantity;
//					}
//公式修改 2017-03-07
                        if (productMaterial.wWidth < 17) {
                            newQuota = (productMaterial.wLong / 100 + productMaterial.wWidth / 100 + 0.07f) * (2 * productMaterial.wWidth / 100 + productMaterial.wHeight / 100 + 0.04f) * 2 * productMaterial.quantity;
                        } else {
                            newQuota = (productMaterial.wLong / 100 + productMaterial.wWidth / 100 + 0.07f) * (productMaterial.wWidth / 100 + productMaterial.pHeight / 100 + 0.04f) * 2 * productMaterial.quantity;
                        }


                        break;

                    case PackMaterialClass.CLASS_INSIDE_BOX:


                        //newQuota = (pLong / 100 + pWidth / 100 + 0.07f) * (pWidth / 100 + pHeight / 100 + 0.04f) * 2 * quantity;
                        //公式修改 2017-03-07
                        if (productMaterial.wWidth < 6.5) {
                            newQuota = (productMaterial.wLong / 100 + productMaterial.wWidth / 100 + 0.07f) * (2 * productMaterial.wWidth / 100 + productMaterial.wHeight / 100 + 0.04f) * 2 * productMaterial.quantity;
                        } else {
                            newQuota = (productMaterial.wLong / 100 + productMaterial.wWidth / 100 + 0.07f) * (productMaterial.wWidth / 100 + productMaterial.pHeight / 100 + 0.04f) * 2 * productMaterial.quantity;
                        }
                        break;


                    case PackMaterialClass.CLASS_CAIHE:
                        // 彩盒计算
                      //  newQuota = (pLong / 100 + pWidth / 100 * 4) * (pWidth / 100 + pHeight / 100 + 0.06f) * 2 * quantity;

                        //20170820 彩盒去除公司，並且單價可填寫
                        newQuota = productMaterial.quantity;
                        break;
                    case PackMaterialClass.CLASS_ZHANSHIHE:

                        newQuota = (productMaterial.pLong / 100 + productMaterial.pWidth / 100 * 2 + 0.42f) * (productMaterial.pWidth / 100 * 4 + productMaterial.pHeight / 100 + 0.02f) * productMaterial.quantity;

                        break;

                    case PackMaterialClass.CLASS_JIAODAI:
                        //TODO  胶带计算公式

                        //这个计算公式独立计算。即有关联其他材料进行计算  @link  updateJiaodaiQuota


                        break;
                    case PackMaterialClass.CLASS_TESHU_BAOLILONG:
                        //TODO  保丽隆计算公式

                        //这个计算公式独立计算。即有关联其他材料进行计算  @link  updateJiaodaiQuota
                        break;

                    case PackMaterialClass.CLASS_QIPAODAI:

                        //气泡袋双面  长宽高x2
                        newQuota = defaultCalculateQuota(productMaterial)*2;

                        break;


                    default:

                        newQuota = defaultCalculateQuota(productMaterial);
                        break;

                }

            } else {
                newQuota = defaultCalculateQuota(productMaterial);
            }


        }

        //加上计算单位换算比例（）
        productMaterial.quota = FloatHelper.scale(newQuota * productMaterial.unitRatio, 5);


        productMaterial.amount = FloatHelper.scale(productMaterial.quota * productMaterial.price);


    }

    /**
     * 设置新材料  更新各项数据
     *
     * @param item
     * @param material
     */
    public static void updateMaterial(ProductMaterial item, Material material) {

        item.materialCode = material.code;
        item.materialName = material.name;
        item.materialId = material.id;

        item.price = FloatHelper.scale(material.price);
        item.available = material.available;

        item.unitName = material.unitName;
        item.unit2 = material.unit2;
        item.price2 = material.price2;
        item.type = material.typeId;

        item.mWidth = material.wWidth;
        item.mHeight = material.wHeight;
        item.mLong = material.wLong;
        item.classId = material.classId;
        item.className = material.className;
        item.memo = material.memo;
        item.unitRatio = material.unitRatio;
        update(item);


    }

    public static void setPackMaterialClass(ProductMaterial item, PackMaterialClass packMaterialClass) {
        if (packMaterialClass == null) return;
        item.packMaterialClass = packMaterialClass;

        item.packMaterialType = packMaterialClass.type;
        item.packMaterialPosition = packMaterialClass.position;


    }

    /**
     * 更新胶带计算量
     * chuanru
     *
     * @param productMaterial
     * @param neiheMaterial
     */
    public static void updateJiaodaiQuota(ProductMaterial productMaterial, Product product, ProductMaterial neiheMaterial) {


        //PackMaterialClass mPackMaterialClass=neiheMaterial.getPackMaterialClass();

        //验证 当前材料是 封口胶带类       关联的材料是 内盒子


        float boxQuantity = neiheMaterial == null ? 0 : neiheMaterial.quantity;
        float boxLong = neiheMaterial == null ? 0 : neiheMaterial.pLong;
        float boxWidth = neiheMaterial == null ? 0 : neiheMaterial.pWidth;
        float boxHeight = neiheMaterial == null ? 0 : neiheMaterial.pHeight;


        //内盒胶带用量
        float boxQuota = 0;
        //装箱胶带用量
        float newQuota = 0;
        int packIndex = product.pack == null ? 0 : product.pack.pIndex;
        switch (packIndex) {
            //折叠盒包装的胶带公式
            case Pack.PACK_XIANKANG_ZHEDIE:

                //不计算内盒胶带用量
                boxQuota = 0;
                //装箱数大于0
                if (product.packQuantity > 0) {
                    if (productMaterial.pWidth >= 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 2) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth >= 20 && productMaterial.pLong >= 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 4) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 2) * productMaterial.quantity / 100f;
                    } else {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 8) * productMaterial.quantity / 100f;
                    }

                }


                break;

            //咸康加强的胶带公式
            case Pack.PACK_XIANKANG_JIAQIANG_SHUAIXIANG:
            case Pack.PACK_XIANKANG_PUTONG_SHUAIXIANG:


                //不计算内盒胶带用量

                //装箱数大于0
                if (product.packQuantity > 0) {
                    if (productMaterial.pWidth >= 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 2) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth >= 20 && productMaterial.pLong >= 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 4) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 2) * productMaterial.quantity / 100f;
                    } else {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 8) * productMaterial.quantity / 100f;
                    }

                }

                //内盒胶带用量
                if (boxQuantity > 0) {

                    if (productMaterial.pLong < 80 && productMaterial.pWidth >= 20) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4) * boxQuantity / 100f;
                    } else if (productMaterial.pLong >= 80 && productMaterial.pWidth >= 20) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4 + (boxWidth + 20) * 2) * boxQuantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong < 80) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4 + (boxWidth + 20) * 2) * boxQuantity / 100f;
                    } else

                    {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4 + (boxWidth + 20) * 8) * boxQuantity / 100f;
                    }


                }


                break;


            //默认普通公式
            default:

                //计算内盒胶带用量
                if (boxQuantity > 0) {
                    if (productMaterial.pLong < 80 && productMaterial.pWidth >= 20) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4) * boxQuantity / 100f;
                    } else if (productMaterial.pLong >= 80 && productMaterial.pWidth >= 20) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4 + (boxWidth + 20) * 2) * boxQuantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong < 80) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4) * boxQuantity / 100f;
                    } else if (productMaterial.pLong >= 80 && productMaterial.pWidth < 20) {
                        boxQuota = ((boxLong + 20) * 2 + (boxWidth + 10) * 4 + (boxWidth + 20) * 4) * boxQuantity / 100f;
                    }

                }
                //装箱数大于0
                if (product.packQuantity > 0) {
                    if (productMaterial.pWidth >= 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth >= 20 && productMaterial.pLong >= 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 2) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong < 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4) * productMaterial.quantity / 100f;
                    } else if (productMaterial.pWidth < 20 && productMaterial.pLong >= 80) {
                        newQuota = ((productMaterial.pLong + 20) * 2 + (productMaterial.pWidth + 10) * 4 + (productMaterial.pWidth + 20) * 4) * productMaterial.quantity / 100f;
                    }

                }


        }


        Logger.getLogger("TEST").info("boxQuota:" + boxQuota + ",newQuota:" + newQuota);
        productMaterial.quota = FloatHelper.scale(newQuota + boxQuota, 5);


        productMaterial.amount = FloatHelper.scale(productMaterial.quota * productMaterial.price);


    }

    /**
     * 更新保丽龙的材料成本
     */
    public static void updateBAOLILONGQuota(ProductMaterial productMaterial, Product product, ProductMaterial waixiang) {

        if (product == null) return;
        float waixiangLong = waixiang.pLong;
        float waixiangWidth = waixiang.pWidth;
        float waixiangHeight = waixiang.pHeight;
        Xiankang xiankang = product.xiankang;

        int unitSize = 1;
        try {
            unitSize = Integer.valueOf(product.pUnitName.substring(product.pUnitName.lastIndexOf("/") + 1));
        } catch (Throwable t) {
        }

        float newQuota = 0;
        int packIndex = product.pack == null ? 0 : product.pack.pIndex;
        switch (packIndex) {

            //折叠盒包装的胶带公式
            case Pack.PACK_XIANKANG_ZHEDIE:


                newQuota = waixiangLong * waixiangHeight / 10000 * 2.54f / 100f * (xiankang.pack_front + xiankang.pack_middle * (unitSize - 1));
                break;

            //咸康加强的胶带公式
            case Pack.PACK_XIANKANG_JIAQIANG_SHUAIXIANG:
            case Pack.PACK_XIANKANG_PUTONG_SHUAIXIANG:
                //newQuota=	waixiangLong*waixiangHeight/10000*2.54f/100f*(xiankang.pack_front+xiankang.pack_middle*(unitSize-1));
                if (xiankang.pack_perimeter > 0 && xiankang.pack_front_back > 0 && xiankang.pack_middle > 0) {
                    newQuota = waixiangLong * waixiangWidth / 10000f * xiankang.pack_perimeter * 2.54f / 100f * 2
                            + waixiangHeight * waixiangWidth / 10000f * xiankang.pack_perimeter * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_front_back * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_middle * 2.54f / 100f * (unitSize - 1);
                } else if (xiankang.pack_perimeter > 0 && xiankang.pack_front_back > 0) {

                    newQuota = waixiangLong * waixiangWidth / 10000f * xiankang.pack_perimeter * 2.54f / 100f * 2
                            + waixiangHeight * waixiangWidth / 10000f * xiankang.pack_perimeter * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_front_back * 2.54f / 100f * 2;
                } else if (xiankang.pack_cube > 0 && xiankang.pack_middle > 0) {
                    newQuota = waixiangLong * waixiangWidth / 10000f * xiankang.pack_cube * 2.54f / 100f * 2
                            + waixiangHeight * waixiangWidth / 10000f * xiankang.pack_cube * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_cube * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_middle * 2.54f / 100f * (unitSize - 1);
                } else if (xiankang.pack_cube > 0) {
                    newQuota = waixiangLong * waixiangWidth / 10000f * xiankang.pack_cube * 2.54f / 100f * 2
                            + waixiangHeight * waixiangWidth / 10000f * xiankang.pack_cube * 2.54f / 100f * 2
                            + waixiangLong * waixiangHeight / 10000f * xiankang.pack_cube * 2.54f / 100f * 2;
                }


                break;


            default:

                newQuota = 0;


        }


        productMaterial.quota = newQuota;


        productMaterial.amount = FloatHelper.scale(productMaterial.quota * productMaterial.price);

    }

    /**
     * 更新油漆的汇总信息
     * @param product
     * @param paintCost
     * @param paintWage
     */
    public static void updatePaintData(Product product, float paintCost, float paintWage, GlobalData globalData) {

        product.paintCost=FloatHelper.scale(paintCost);
        product.paintWage=FloatHelper.scale(paintWage);

        //各道材料++修理工资+搬运工资
        updateProductInfoOnly(globalData, product);


    }

    /**
     * 根据外厂实际成本值 港杂费用 更新到 成本价 出厂价，美金价格
     * @param product
     * @param productCost  实际成本
     *
     */
    public static void updateCostOnForeignFactory(Product product, GlobalData globalData, float productCost) {


        product.productCost=productCost;


//		计算外厂港杂

//		calculateForeignGangza();
//		this.gangza=gangza;







        //更新成本价格
        product.cost =FloatHelper.scale(productCost+ product.gangza);




        //更新出厂价格(加上管理费用  即 除以换算比率)外厂是管理费用
        product.price =FloatHelper.scale(product.cost *(1+globalData.manageRatioForeign));


        //更新美金价格
        product.fob =FloatHelper.scale(product.price /globalData.exportRate);



    }

    /**
     * 更新包装相关数据 影响到volume值
     * @param product
     * @param inboxCount
     * @param quantity
     * @param packLong
     * @param packWidth
     * @param packHeight
     */
    public static void updatePackData(Product product, GlobalData globalData, int inboxCount, int quantity, float packLong, float packWidth, float packHeight) {



        product.insideBoxQuantity=inboxCount;
        product.packQuantity=quantity;
        product.packLong=packLong;
        product.packWidth=packWidth;
        product.packHeight=packHeight;


        //计算体积
        product.packVolume = FloatHelper.scale(packLong*packWidth*packHeight/1000000,3);


        //外厂才计算港杂
        //计算出港杂      立方数* 出口运费/装箱数

        product.gangza=FloatHelper.scale(product.packQuantity>0? globalData.price_of_export* product.packVolume/ product.packQuantity:0);

        updateCostOnForeignFactory(product, globalData, product.productCost);


    }

    /**
     * 计算外厂相关数据
     * @param product
     * @param globalData
     */
    public static void updateForeignFactoryRelate(Product product, GlobalData globalData)
    {

        product.gangza =FloatHelper.scale(product.packQuantity >0? globalData.price_of_export* product.packVolume / product.packQuantity :0);
        updateCostOnForeignFactory(product, globalData, product.productCost);

    }

    /**
     * 更新配料洗刷枪的费用的数据量值。
     *
     * @return  更新记录所在位置index'
     */
    public static int updateQuantityOfIngredient(List<ProductPaint> productPaints, GlobalData globalData)
    {


        ProductPaint ingredientPaint=null;
        float totalIngredientQuantity=0;
        for(ProductPaint paint: productPaints)
        {

            if( paint.processName==null|| !paint.processName.contains(ProductProcess.XISHUA)  )
            {
                if(paint.materialId>0)
                    totalIngredientQuantity+=paint.ingredientQuantity;
            }else
                ingredientPaint=paint;

        }

        if(ingredientPaint!=null)
        {




            ingredientPaint.quantity=FloatHelper.scale(totalIngredientQuantity *globalData.extra_ratio_of_diluent, 3);

            updateProductPaintPriceAndCostAndQuantity(ingredientPaint, globalData);


            int index= productPaints.indexOf(ingredientPaint);
            return index;
            //fireTableRowsUpdated(flowStep, flowStep);
        }


        return -1;


    }

    /**
     * 更新产品的统计数据
     */
    public static void updateProductStatistics(ProductDetail productDetail, GlobalData globalData)
    {




        float paintWage = 0;
        float paintCost = 0;

        final Product product = productDetail.product;
        for (ProductPaint paint : productDetail.paints) {
            paintWage += paint.processPrice;
            paintCost += paint.cost  ;

        }

        updatePaintData(product, paintCost, paintWage,
            globalData);


        //汇总计算白胚材料
        float conceptusCost = 0;
        for (ProductMaterial material : productDetail.conceptusMaterials) {
            conceptusCost += material.getAmount();
        }
        product.conceptusCost = FloatHelper.scale(conceptusCost);
        //汇总计算组白胚工资
        float conceptusWage = 0;
        for (ProductWage wage : productDetail.conceptusWages) {
            conceptusWage += wage.getAmount();
        }
        product.conceptusWage = FloatHelper.scale(conceptusWage);


        //汇总计算组装材料
        float assembleCost = 0;
        for (ProductMaterial material : productDetail.assembleMaterials) {
            assembleCost += material.getAmount();
        }
        product.assembleCost = FloatHelper.scale(assembleCost);

        //汇总计算组装工资
        float assembleWage = 0;
        for (ProductWage wage : productDetail.assembleWages) {
            assembleWage += wage.getAmount();
        }
        product.assembleWage = FloatHelper.scale(assembleWage);

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
        product.packCost = FloatHelper.scale(product.packQuantity==0?packCost:packCost/ product.packQuantity);

        //汇总计算包装工资
        float packWage = 0;
        for (ProductWage wage : productDetail.packWages) {
            packWage += wage.getAmount();
        }
        //计算包装工资 平摊箱数， 如无箱数 则默认1
        product.packWage = FloatHelper.scale(product.packQuantity==0?packWage:packWage/ product.packQuantity);





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
        for(Summariable summariable: productDetail.summariables)
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

        product.cost1=FloatHelper.scale(cost1);
        product.cost5=FloatHelper.scale(cost5);
        product.cost6=FloatHelper.scale(cost6);
        product.cost7=FloatHelper.scale(cost7);
        product.cost8=FloatHelper.scale(cost8);
        product.cost11_15=FloatHelper.scale(cost11_15);

        //计算包装材料 平摊箱数， 如无箱数 则默认1
        product.cost4=FloatHelper.scale(product.packQuantity==0?cost4:cost4/ product.packQuantity);









        /**
         * 重新计算总成本
         */


        updateProductInfoOnly(globalData, product);


    }
}
