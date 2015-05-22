package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.MaterialRepository;
import com.giants3.hd.server.repository.PrdtRepository;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.Prdt;
import com.giants3.hd.utils.entity.Prdt1;
import com.giants3.hd.utils.entity.PrdtResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* 材料信息控制类。
*/
@Controller
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    List<Material> listPrdtJson(ModelMap model)   {


//        for (Prdt1 prdt1 : materialRepository.findAll()) {
//            Gson gson=new Gson( );
//            array.add(  gson.toJsonTree(prdt1));
//        }
        return materialRepository.findAll();
    }

    //   /api/prdts/2.209e%2B007     这个 。 请求中会出现错误    实际中  prd_no 得到的参数是2
    @RequestMapping(value = "/{prd_no:.+}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Material> listPrdtJson(@PathVariable String prd_no)   {


        return  materialRepository.findByCodeLike(prd_no);


    }





}
