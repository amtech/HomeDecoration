package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.CustomerRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.Customer;
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
@RequestMapping("/customer")
public class CustomerController extends BaseController{


    @Autowired
    private CustomerRepository customerRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Customer> list( )   {

        return  wrapData(customerRepository.findAll());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Customer> save(@RequestBody  List<Customer> customers)   {


        for(Customer customer: customers)
        {


            Customer oldData=customerRepository.findFirstByCodeEquals(customer.code);
            if(oldData==null)
            {
                customer.id=-1;


            }else
            {
                customer.id=oldData.id;



            }
            customerRepository.save(customer);



        }


        return  list();
    }



}
