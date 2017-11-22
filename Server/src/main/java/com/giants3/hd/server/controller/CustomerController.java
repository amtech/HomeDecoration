package com.giants3.hd.server.controller;


import com.giants3.hd.server.service.CustomerService;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.exception.HdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {




    @Autowired
    private CustomerService customerService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Customer> list() {

        return wrapData(customerService.list());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Customer> save(@RequestBody List<Customer> customers) {

        try {
            return wrapData(customerService.saveCustomers(customers));
        } catch (HdException e) {

          return  wrapError(e.getMessage());
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Customer> add(@RequestParam(value="name",required = false,defaultValue = "") String name
                              ,@RequestParam(value="code",required = false,defaultValue = "") String code
                              ,@RequestParam(value="tel",required = false,defaultValue = "") String tel

    ) {


       return      customerService.add(name,code,tel);

    }
}
