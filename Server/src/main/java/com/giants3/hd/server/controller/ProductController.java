package com.giants3.hd.server.controller;


import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.repository.ProductRepository;

import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* 产品信息
*/
@Controller
@RequestMapping("/product")
public class ProductController {

    private static final int NUMBER_OF_PERSONS_PER_PAGE = 20;
    @Autowired
    private ProductRepository productRepository;


    Gson gson=new GsonBuilder().create();




    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> listPrdtJson(ModelMap model)   {


        return productRepository.findAll();
    }

    //   /api/prdts/2.209e%2B007     这个 。 请求中会出现错误    实际中  prd_no 得到的参数是2
    @RequestMapping(value = "/search", method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product>   listPrdtJson(@RequestParam(value = "proName",required = false,defaultValue ="") String prd_name
    ,@RequestParam(value = "pageIndex",required = false,defaultValue ="0") int pageIndex,@RequestParam(value = "pageSize",required = false,defaultValue =  "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable=constructPageSpecification(pageIndex, pageSize);
          Page<Product> pageValue=  productRepository.findByPrd_noLike("%"+prd_name+"%", pageable);

        List<Product> products=pageValue.getContent();

        RemoteData<Product> productRemoteData=new  RemoteData<Product>();
        productRemoteData.datas.addAll(products);
        productRemoteData.pageCount=pageValue.getTotalPages();
        productRemoteData.pageIndex=pageIndex;
        productRemoteData.pageSize=pageSize;
        return productRemoteData;




//        Type generateType = new TypeToken<RemoteData<Product>>() {
//        }.getType();
//        String result = gson.toJson(productRemoteData, generateType);
//        return result;



    }

    @RequestMapping( value = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    Product findProdcutById(@RequestParam("id") int productId)   {
        return productRepository.findByPrdId(productId);
    }


    /**
     * 查询产品的详细信息
     * 包括 包装信息
     *      物料清单（胚体，油漆，包装）
     *
     * @param productId
     * @return
     */
    @RequestMapping( value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    ProductDetail findProductDetailById(@RequestParam("id") int productId)   {




        Product product= productRepository.findByPrdId(productId);
        ProductDetail detail=new ProductDetail();
        detail.product=product;
        return detail;
    }


    /**
     * 产品完整信息的保存
     *
     *  @param productDetail 产品全部信息
     * @return
     */
    @RequestMapping( value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData findProductDetailById(@RequestBody ProductDetail productDetail)   {



        long productId=productDetail.product.id;

        //新增加的产品数据
        Product newProduct=productDetail.product;

        /**
         * 未生成id 添加记录
          */
     if(productId<=0)
     {






     }else
     {
        //找出旧的记录
         Product oldData=productRepository.findByPrdId(productId);

         //如果产品名称修改  则修正缩略图
         if(!oldData.name.equals(newProduct.name))
         {


         }














     }


        //更新图片数据库
      if(!StringUtils.isEmpty(productDetail.product.name))
      {



      }









    RemoteData data=new RemoteData();

        return  data;
    }



    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The index of the wanted result page
     * @return
     */
    private Pageable constructPageSpecification(int pageIndex) {
        return constructPageSpecification(pageIndex, NUMBER_OF_PERSONS_PER_PAGE);
    }
    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The index of the wanted result page
     * @return
     */
    private Pageable constructPageSpecification(int pageIndex,int pageSize) {
        Pageable pageSpecification = new PageRequest(pageIndex, NUMBER_OF_PERSONS_PER_PAGE, sortByLastNameAsc());
        return pageSpecification;
    }

    /**
     * Returns a Sort object which sorts persons in ascending order by using the last name.
     * @return
     */
    private Sort sortByLastNameAsc() {
        return new Sort(Sort.Direction.ASC, "name");
    }
}
