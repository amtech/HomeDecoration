package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.repository.Prdt1Repository;
import com.giants3.hd.server.repository.PrdtRepository;
import com.giants3.hd.server.repository.ProductRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* 产品信息
*/
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;





    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> listPrdtJson(ModelMap model)   {


        return productRepository.findAll();
    }

    //   /api/prdts/2.209e%2B007     这个 。 请求中会出现错误    实际中  prd_no 得到的参数是2
    @RequestMapping(value = "/search/{prd_name}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> listPrdtJson(@PathVariable String prd_name)   {
        return  productRepository.findByPrd_noLike(prd_name);
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




}
