package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.MaterialRepository;

import com.giants3.hd.server.repository.ProductMaterialRepository;
import com.giants3.hd.server.repository.ProductPaintRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Autowired
    private JpaRepository<MaterialClass,Long> materialClassRepository;


    @Autowired
    private JpaRepository<MaterialType,Long> materialTypeRepository;

    @Autowired
    private JpaRepository<MaterialEquation,Long> equationRepository;



    @Autowired
    private ProductMaterialRepository productMaterialRepository;


    @Autowired
    private ProductPaintRepository productPaintRepository;

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




    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Material> save(@RequestBody   Material  material  )   {



          material=  materialRepository.save(material);

        return wrapData(material);
    }

    @RequestMapping(value = "/findListByCodes",method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByCodes(@RequestBody  List<String> codes )   {



        List<Material> materials=new ArrayList<>();
        for(String code:codes)
        {


            Material data=materialRepository.findByCodeEquals(code);
            if(null!=data)
                materials.add(data);



        }



        return wrapData(materials);
    }


    @RequestMapping(value = "/findListByNames",method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByNames(@RequestBody  List<String> names )   {



        List<Material> materials=new ArrayList<>();
        for(String name:names)
        {


            Material data=materialRepository.findByNameEquals(name);
            if(null!=data)
                materials.add(data);



        }



        return wrapData(materials);
    }





    /**
     * 获取材料类型类别
     * @return
     */

    @RequestMapping(value = "/listClass",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialClass> listClass( )   {



        return wrapData(materialClassRepository.findAll());
    }



    /**
     * 获取材料类型类别
     * @return
     */

    @RequestMapping(value = "/listType",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialType> listType( )   {



        return wrapData(materialTypeRepository.findAll());
    }


    /**
     * 获取材料计算公式。
     * @return
     */

    @RequestMapping(value = "/listEquation",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialEquation> listEquation( )   {

        return wrapData(equationRepository.findAll());
    }



    /**
     *删除产品信息
     *
     *
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData< Void> logicDelete(@RequestParam("id") long materialId ) {




        //查询是否有产品使用该物料。
        if(productMaterialRepository.findByMaterialIdEquals(materialId)!=null )
        {


            return     wrapError("该材料在产品材料中有使用 ，不能删除 ！");
        }
        //查询是否有产品使用该物料。
        if(productPaintRepository.findByMaterialIdEquals(materialId)!=null )
        {


            return     wrapError("该材料在产品油漆中有使用 ，不能删除 ！");
        }



        Material material=materialRepository.findOne(materialId);
        if(material==null)
        {
            return     wrapError("该材料已经删除， 请更新 ！");
        }

        materialRepository.delete(materialId);

        //TODO   save the delete item to the wareHouse .




        return wrapData( );
    }

}
