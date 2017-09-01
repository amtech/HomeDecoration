package com.giants3.hd.server.controller;


import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Pack;
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
@RequestMapping("/pack")
public class PackController extends BaseController {

    @Autowired
    private JpaRepository<Pack,Long> packMRepository;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Pack> list( )   {


        return wrapData(packMRepository.findAll());
    }

}
