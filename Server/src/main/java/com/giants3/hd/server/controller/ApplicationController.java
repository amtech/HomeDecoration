package com.giants3.hd.server.controller;

import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.GlobalDataService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.noEntity.RemoteData;

import com.giants3.hd.entity.*;
import com.giants3.hd.utils.FloatHelper;
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
    private GlobalDataService globalDataService;
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
    RemoteData<GlobalData> setGlobal(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody GlobalData globalData) {

        GlobalData oldData = globalDataService.find(globalData.id);
        if (oldData == null) {

            return wrapError("系统异常");
        }


        if (oldData.isGlobalSettingEquals(globalData)) {


            return wrapError("全局参数无改变");


        }
        //判断是否产品相关的材料信息改变
        boolean isProductRelateChange=!oldData.isProductRelateDataEquals(globalData);




        if(isProductRelateChange) {


            //判断是否材料相关的固定参数改变
            boolean materialRelateChanged =!oldData.isMaterialRelateEquals(globalData);


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
                        ProductAnalytics.updateForeignFactoryRelate(product, globalData);
                    } else

                        //如果有跟油漆材料相关 先统计油漆相关数据
                        if (materialRelateChanged) {






                            List<ProductPaint> productPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(product.id);
                            ProductAnalytics.updateProductWithProductPaints(product,productPaints,globalData);



                            //更新油漆数据
                            productPaintRepository.save(productPaints);
                            productPaintRepository.flush();


                            //更新产品油漆统计数据 自动联动更新全局数据。






                        }

                    //各道材料++修理工资+搬运工资
                            ProductAnalytics.updateProductInfoOnly(globalData,product);






                    //保存全部数据
                    //重新保存数据
                    productRepository.save(product);



                    //增加历史操作记录
                    OperationLog operationLog = OperationLog.createForGloBalDataChange(product, user);
                    operationLogRepository.save(operationLog);

                }
                if(productPage.getSize()>0) {
                    productRepository.flush();
                    operationLogRepository.flush();
                }

            } while (productPage.hasNext());


        }
        //更新全局数据
      GlobalData  saveResult=  globalDataService .save(globalData);


        return wrapData(saveResult);
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