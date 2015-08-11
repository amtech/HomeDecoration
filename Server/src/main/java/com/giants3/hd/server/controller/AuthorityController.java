package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.AuthorityRepository;
import com.giants3.hd.server.repository.ModuleRepository;
import com.giants3.hd.server.repository.UserRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Authority;
import com.giants3.hd.utils.entity.Customer;
import com.giants3.hd.utils.entity.Module;
import com.giants3.hd.utils.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 权限
* Created by davidleen29 on 2014/9/18.
*/
@Controller
@RequestMapping("/authority")
public class AuthorityController extends  BaseController{





    @Autowired
    private AuthorityRepository authorityRepository;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRepository userRepository;
    @RequestMapping(value="/findByUser", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Authority> findByUser( @RequestParam(value = "userId" ) long userId)   {



        User user=userRepository.findOne(userId);

        List<Module> modules=moduleRepository.findAll();
        List<Authority> authorities=authorityRepository.findByUser_IdEquals(userId);
        List<Authority> unConfigAuthorities=new ArrayList<>();
        int moduleSize=modules.size();
        for (int i = 0; i < moduleSize; i++) {

            Module module=modules.get(i);
            boolean found=false;
            for(Authority authority:authorities)
            {
                if(authority.module.id==module.id
                        )
                {
                    found=true;
                    break;
                }
            }

            if(!found)
            {
                Authority authority=new Authority();
                authority.module=module;
                authority.user=user;
                unConfigAuthorities.add(authority);

            }


        }

        authorities.addAll(unConfigAuthorities);



     return    wrapData(authorities  );





    }


    @RequestMapping(value="/saveList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Authority> save( @RequestParam(value = "userId" ) long userId,@RequestBody List<Authority> authorities)   {


        User user=userRepository.findOne(userId);
        List<Authority> newData=new ArrayList<>();

        for(Authority authority : authorities)
        {
            authority.user=user;
            newData.add( authorityRepository.save(authority));
        }


        return  wrapData(newData);
    }



    @RequestMapping(value="/login", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<User> login(  @RequestBody User user)   {


        List<User> userList=userRepository.findByNameEquals(user.name);

        int size=userList.size();
        if(size<=0)
        return     wrapError("用户账户不存在");
        if(size>1)
            return     wrapError("存在重名用户，请联系管理员");

        User findUser=userList.get(0);
        if(!findUser.password.equals(user.password))
        {
            return     wrapError("密码错误");
        }



        return  wrapData(findUser);
    }

    @RequestMapping(value="/moduleList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Module> moduleList( )   {





        return    wrapData(moduleRepository.findAll()  );





    }



}
