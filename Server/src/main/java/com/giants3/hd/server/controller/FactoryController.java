package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.FactoryRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* 产品类别
*/
@Controller
@RequestMapping("/factory")
public class FactoryController extends BaseController{


    @Autowired
    private FactoryRepository factoryRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Factory> list( )   {

        return  wrapData(factoryRepository.findAll());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Factory> save(@RequestBody  List<Factory> factories)   {


        for(Factory factory : factories)
        {


            Factory oldData= factoryRepository.findFirstByCodeEquals(factory.code);
            if(oldData==null)
            {
                factory.id=-1;


            }else
            {
                factory.id=oldData.id;



            }
            factoryRepository.save(factory);



        }


        return  list();
    }



}
