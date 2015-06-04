package com.giants3.hd.server.controller;


import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.PClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
* 产品类别
*/
@Controller
@RequestMapping("/productClass")
public class ProductClassController extends BaseController{


    @Autowired
    private JpaRepository<PClass,Long> productClassRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PClass> list( )   {

        return  wrapData(productClassRepository.findAll());
    }




}
