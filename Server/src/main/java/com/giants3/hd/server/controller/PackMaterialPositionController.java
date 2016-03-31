package com.giants3.hd.server.controller;


import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.PackMaterialPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* 材料信息控制类。
*/
 @Controller
@RequestMapping("/packMaterialPosition")
public class PackMaterialPositionController extends BaseController {

    @Autowired
    private JpaRepository<PackMaterialPosition,Long> packMaterialPositionJpaRepository;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PackMaterialPosition> list( )   {


        return wrapData(packMaterialPositionJpaRepository.findAll());
    }

}
