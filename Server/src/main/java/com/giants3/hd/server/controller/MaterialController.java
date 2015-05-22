package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.MaterialRepository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;

import com.giants3.hd.utils.entity.Product;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Basic;

import java.util.List;

/**
* 材料信息控制类。
*/
@Controller
@RequestMapping("/material")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialRepository materialRepository;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> listPrdtJson(ModelMap model)   {



        return wrapData(materialRepository.findAll());
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> listPrdtJson(@RequestParam(value = "codeOrName",required = false,defaultValue ="") String codeOrName
            ,@RequestParam(value = "pageIndex",required = false,defaultValue ="0") int pageIndex,@RequestParam(value = "pageSize",required = false,defaultValue =  "20") int pageSize)   {

        Pageable pageable=constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.ASC,"code"));
        String searchValue="%" + codeOrName.trim() + "%";
        Page<Material>  pageValue=
        materialRepository.findByCodeLikeOrNameLike(searchValue,searchValue, pageable);

        List<Material> materials=pageValue.getContent();
        return  wrapData(pageIndex,pageable.getPageSize(),pageValue.getTotalPages(), (int) pageValue.getTotalElements(),materials);
    }





}
