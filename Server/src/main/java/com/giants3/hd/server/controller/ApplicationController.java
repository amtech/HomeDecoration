package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.ProductPaintArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ProductController productController;

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




        //与材料相关的固定参数改变
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




                ProductDetail productDetail=productController.findProductDetailById(product.id);
                if(materialRelateChanged)
                {


                    //更新油漆材料中稀释剂用量
                    list.clear();
                    list.addAll(productDetail.paints);
                    list.updateQuantityOfIngredient(globalData);
                    list.clear();
                    // productDetail.paints
                    for(ProductPaint paint:productDetail.paints)
                    {
                        paint.updatePriceAndCostAndQuantity(globalData);
                    }
                }


                //更新产品汇总信息
                productDetail.updateProductStatistics(globalData);




                //保存全部数据
                productController.saveProductDetail(user,productDetail);



                //增加历史操作记录
                OperationLog   operationLog= OperationLog.createForGloBalDataChange(product, user);
                operationLogRepository.save(operationLog);

            }




        }while (productPage.hasNext());




        //更新全局数据
        globalDataRepository.save(globalData );



        return wrapData(globalData);
    }

}