package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.utils.noEntity.ProductPaintArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 应用程序相关控制类
* Created by davidleen29 on 2014/9/18.
*/
@Controller

@RequestMapping("/application")
public class ApplicationController extends  BaseController {


    @Autowired
    private GlobalDataRepository globalDataRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private ProductPaintRepository productPaintRepository;




    @RequestMapping(value = "/setGlobal", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<GlobalData> findListByNames(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestBody GlobalData globalData) {

        GlobalData oldData = globalDataRepository.findOne(globalData.id);
        if (oldData == null) {

            return wrapError("系统异常");
        }


        if (oldData.equals(globalData)) {



            return wrapError("全局参数无改变");


        }




        //判断是否材料相关的固定参数改变
        boolean materialRelateChanged=Float.compare(oldData.extra_ratio_of_diluent,globalData.extra_ratio_of_diluent)!=0||Float.compare(oldData.price_of_diluent,globalData.price_of_diluent)!=0;







        //
        ProductPaintArrayList list=new ProductPaintArrayList();
        //遍历所有产品数据
        int pageIndex          =0;
        int pageSize=10;

        Page<Product> productPage=null;

        do{
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
            productPage= productRepository.findAll(pageable);

            //一次处理10条
            for(Product product:productPage.getContent())
            {





                //如果有跟油漆材料相关
                if(materialRelateChanged)
                {


                    List<ProductPaint> productPaints=productPaintRepository.findByProductIdEquals(product.id);

                    //更新油漆材料中稀释剂用量
                    list.clear();
                    list.addAll(productPaints);
                    list.updateQuantityOfIngredient(globalData);
                    list.clear();

                    //重新计算各油漆的单价 金额
                    for(ProductPaint paint:productPaints)
                    {
                        paint.updatePriceAndCostAndQuantity(globalData);
                    }



                    //更新油漆数据
                    productPaintRepository.save(productPaints);
                    productPaintRepository.flush();

                    //汇总计算油漆单价 金额
                    float paintWage = 0;
                    float paintCost = 0;
                    for (ProductPaint paint : productPaints) {
                        paintWage += paint.processPrice;
                        paintCost += paint.cost  ;

                    }
                    //更新产品油漆统计数据 自动联动更新全局数据。
                    product.updatePaintData(paintCost, paintWage,
                            globalData);


                }else
                {
                    product.calculateTotalCost(globalData);
                }







                //保存全部数据
                //重新保存数据
                productRepository.save(product);
                productRepository.flush();


                //增加历史操作记录
                OperationLog   operationLog= OperationLog.createForGloBalDataChange(product, user);
                operationLogRepository.saveAndFlush(operationLog);

            }




        }while (productPage.hasNext());




        //更新全局数据
        globalDataRepository.save(globalData );



        return wrapData(globalData);
    }

}