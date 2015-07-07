package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.SalesmanRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Salesman;
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
@RequestMapping("/salesman")
public class SalesmanController extends BaseController{


    @Autowired
    private SalesmanRepository salesmanRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Salesman> list( )   {

        return  wrapData(salesmanRepository.findAll());
    }


    @RequestMapping(value="/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Salesman> save(@RequestBody List<Salesman> salesmans)   {


        for(Salesman salesman : salesmans)
        {


            Salesman oldData=salesmanRepository.findFirstByCodeEqualsAndNameEquals(salesman.code,salesman.name);
            if(oldData==null)
            {
                salesman.id=-1;


            }else
            {
                salesman.id=oldData.id;



            }
            salesmanRepository.save(salesman);



        }


        return  list();
    }

}
