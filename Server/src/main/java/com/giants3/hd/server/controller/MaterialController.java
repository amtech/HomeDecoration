package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.MaterialRepository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    RemoteData<Material> list( )   {



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


    @RequestMapping(value = "/saveList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> saveList(@RequestBody  List<Material> materials )   {

        for(Material material:materials)
        {


            Material oldData=materialRepository.findByCodeEquals(material.code);
            if(oldData==null)
            {
                material.id=-1;


            }else
            {
                material.id=oldData.id;

            }

            materialRepository.save(material);


        }



        return wrapData(new ArrayList<Void>());
    }


    @RequestMapping(value = "/findListByCodes",method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByCode(@RequestBody  List<String> codes )   {



        List<Material> materials=new ArrayList<>();
        for(String code:codes)
        {


            Material data=materialRepository.findByCodeEquals(code);
            if(null!=data)
                materials.add(data);



        }



        return wrapData(materials);
    }



}
