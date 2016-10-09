package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.noEntity.ProductPaintArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *
 * 应用程序相关控制类
* Created by davidleen29 on 2014/9/18.
*/
@Controller

@RequestMapping("/application")
public class    ApplicationController extends  BaseController {


    @Autowired
    private GlobalDataRepository globalDataRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private ProductPaintRepository productPaintRepository;

    @Autowired
    private XiankangRepository xiankangRepository;

    @Autowired
    private Xiankang_JingzaRepository xiankang_jingzaRepository;




    @RequestMapping(value = "/setGlobal", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<GlobalData> findListByNames(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody GlobalData globalData) {

        GlobalData oldData = globalDataRepository.findOne(globalData.id);
        if (oldData == null) {

            return wrapError("系统异常");
        }


        if (oldData.equals(globalData)) {


            return wrapError("全局参数无改变");


        }


        //判断是否材料相关的固定参数改变
        boolean materialRelateChanged = Float.compare(oldData.extra_ratio_of_diluent, globalData.extra_ratio_of_diluent) != 0 || Float.compare(oldData.price_of_diluent, globalData.price_of_diluent) != 0;


        //临时缓存数据
        ProductPaintArrayList list = new ProductPaintArrayList();
        //遍历所有产品数据
        int pageIndex = 0;
        int pageSize = 10;

        Page<Product> productPage = null;

        do {
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
            productPage = productRepository.findAll(pageable);

            //一次处理10条
            for (Product product : productPage.getContent()) {


                boolean isForeignProduct = !Factory.CODE_LOCAl.equals(product.factoryCode);

                //外厂数据 只需更新 成本值
                if (isForeignProduct) {
                    product.updateForeignFactoryRelate(globalData);
                } else

                    //如果有跟油漆材料相关
                    if (materialRelateChanged) {


                        List<ProductPaint> productPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(product.id);

                        //更新油漆材料中稀释剂用量
                        list.clear();
                        list.addAll(productPaints);
                        list.updateQuantityOfIngredient(globalData);
                        list.clear();

                        //重新计算各油漆的单价 金额
                        for (ProductPaint paint : productPaints) {
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
                            paintCost += paint.cost;

                        }
                        //更新产品油漆统计数据 自动联动更新全局数据。
                        product.updatePaintData(paintCost, paintWage,
                                globalData);


                    } else {


                        product.calculateTotalCost(globalData);


                    }


                //保存全部数据
                //重新保存数据
                productRepository.save(product);
                productRepository.flush();


                //增加历史操作记录
                OperationLog operationLog = OperationLog.createForGloBalDataChange(product, user);
                operationLogRepository.saveAndFlush(operationLog);

            }


        } while (productPage.hasNext());


        //更新全局数据
        globalDataRepository.save(globalData);


        return wrapData(globalData);
    }


    /**  特殊接口，临时使用
     * 更新咸康数据  如果product.xiankang_id 为空或者为0  则 更新值
     * @return
     */
    @RequestMapping(value = "/updateXiankang", method = {RequestMethod.POST,RequestMethod.GET})
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> updateXiankang() {



        //找出所有咸康数据
        List<Xiankang> xiankangs=xiankangRepository.findAll();

        for(Xiankang xiankang:xiankangs)
        {

            if(xiankang.xiankang_jingza==null)
            {


                Xiankang_Jingza newJingza=new Xiankang_Jingza();
                //复制转移的属性
                newJingza.setBeizhu(xiankang.getBeizhu());
                newJingza.setBoliguige_gao(xiankang.getBoliguige_gao());
                newJingza.setBoliguige_kuan(xiankang.getCaizhi());
                newJingza.setCaokuan(xiankang.getCaokuan());
                newJingza.setCaoshen(xiankang.getCaoshen());
                newJingza.setGuaju(xiankang.getGuaju());
                newJingza.setHuangui_gao(xiankang.getHuangui_gao());
                newJingza.setHuangui_kuan(xiankang.getHuangui_kuan());
                newJingza.setBiankuang(xiankang.getBiankuang());
                newJingza.setJingzi_gao(xiankang.getJingzi_gao());
                newJingza.setJingzi_kuan(xiankang.getJingzi_kuan());
                newJingza.setMobian(xiankang.getMobian());
                newJingza.setCaizhi(xiankang.getCaizhi());
                newJingza.setHuaxinbianhao(xiankang.getHuaxinbianhao());
                newJingza.setHuaxinxiaoguo(xiankang.getHuaxinxiaoguo());
                newJingza.setHuaxinchangshang(xiankang.getHuaxinchangshang());

                newJingza=    xiankang_jingzaRepository.save(newJingza);
                xiankang.setXiankang_jingza(newJingza);


                xiankangRepository.save(xiankang);


            }
        }




        return wrapData();
    }





}