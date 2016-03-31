package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.UserRepository;
import com.giants3.hd.utils.RemoteData;

import com.giants3.hd.server.entity.User;
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
    private UserRepository userRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list( )   {

        return  wrapData(userRepository.findByIsSalesman(true));
    }


    @RequestMapping(value="/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<User> save(@RequestBody List<User> salesmans)   {


        for(User salesman : salesmans)
        {


            User oldData= userRepository.findFirstByCodeEqualsAndNameEquals(salesman.code,salesman.name);
            if(oldData==null)
            {
                salesman.id=-1;


            }else
            {
                salesman.id=oldData.id;



            }
            userRepository.save(salesman);



        }


        return  list();
    }

}
