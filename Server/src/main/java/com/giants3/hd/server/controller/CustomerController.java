package com.giants3.hd.server.controller;


import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.PClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* 产品类别
*/
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController{


    @Autowired
    private JpaRepository<Customer,Long> customerRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Customer> list( )   {

        return  wrapData(customerRepository.findAll());
    }




}
