package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;


import com.giants3.hd.utils.noEntity.BufferData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* Created by Administrator on 2014/9/18.
*/
@Controller
@RequestMapping("/user")
public class UserController extends  BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PackMaterialClassRepository packMaterialClassRepository;

    @Autowired
    private PackMaterialPositionRepository packMaterialPositionRepository;


    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private PackMaterialTypeRepository packMaterialTypeRepository;

    @Autowired
    private PackRepository   packRepository;

    @Autowired
    private ProductClassRepository   productClassRepository;

    @Autowired
    private MaterialClassRepository   materialClassRepository;
    @Autowired
    private QuoteAuthRepository   quoteAuthRepository;
    @RequestMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {

        userRepository.delete(userRepository.findOne(userId));

        return "redirect:/";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user, BindingResult result) {

        userRepository.save(user);

        return "redirect:/";
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list( )   {

        return  wrapData(userRepository.findAll());
    }


    @RequestMapping(value = "/saveList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users )   {

        for(User user :users)
        {




            User oldData=userRepository.findOne(user.id);
            if(oldData==null)
            {
                user.id=-1;


                //如果是空数据  略过添加
                if(user.isEmpty())
                {
                    continue;
                }



            }else
            {




                user.id=oldData.id;

            }

            userRepository.save(user);


        }



        return list();
    }




    @RequestMapping(value = "/initData",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<BufferData> initData(@RequestBody User user )   {


        BufferData bufferData=new BufferData();
        bufferData.authorities=authorityRepository.findByUser_IdEquals(user.id);
        bufferData.customers=customerRepository.findAll();

        bufferData.materialClasses=materialClassRepository.findAll();
        bufferData.packMaterialClasses=packMaterialClassRepository.findAll();
        bufferData.packMaterialPositions=packMaterialPositionRepository.findAll();

        bufferData.materialTypes=materialTypeRepository.findAll();
        bufferData.packMaterialTypes=packMaterialTypeRepository.findAll();
        bufferData.packs=packRepository.findAll();
        bufferData.pClasses=productClassRepository.findAll();
        bufferData.salesmans=userRepository.findByIsSalesman(true);
        QuoteAuth quoteAuth=quoteAuthRepository.findFirstByUser_IdEquals(user.id);
        if(quoteAuth==null)
        {
            quoteAuth=new QuoteAuth();
            quoteAuth.user=user;
        }
        bufferData.quoteAuth=quoteAuth;



        return wrapData(bufferData);
    }

    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> initData(@RequestBody String[] map )   {


        long userId= Long.valueOf(map[0]);
        String oldPassword  = map[1];
        String newPassword= map[2];
     User user=userRepository.findOne(userId);
        if(user!=null)
        {

            if(user.password.equals(oldPassword))
            {

                user.password=newPassword;
                userRepository.save(user);
                return wrapData( );
            }else
            {
                return   wrapError("旧密码错误");
            }


        }else
        {

          return   wrapError("要修改的员工不存在");
        }






    }
}
