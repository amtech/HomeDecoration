package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.PackMaterialClassRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.PackMaterialClass;
import com.giants3.hd.utils.entity.PackMaterialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* 材料信息控制类。
*/
 @Controller
@RequestMapping("/packMaterialClass")
public class PackMaterialClassController extends BaseController {

    @Autowired
    private PackMaterialClassRepository packMaterialClassRepository;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PackMaterialClass> list( )   {

        return wrapData(packMaterialClassRepository.findAll());
    }




    @RequestMapping(value = "/saveList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<PackMaterialClass> saveClassList(@RequestBody List<PackMaterialClass> materialClasses)   {

        for(PackMaterialClass materialClass: materialClasses)
        {


            PackMaterialClass oldData=packMaterialClassRepository.findFirstByNameEquals(materialClass.name);
            if(oldData==null)
            {
                materialClass.id=-1;


            }else
            {
                materialClass.id=oldData.id;

            }
            packMaterialClassRepository.save(materialClass);

        }

        return list();
    }

}
