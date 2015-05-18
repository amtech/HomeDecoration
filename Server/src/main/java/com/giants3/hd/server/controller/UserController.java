package com.giants3.hd.server.controller;

import com.giants3.hd.utils.entity.User;
import com.giants3.hd.server.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* Created by Administrator on 2014/9/18.
*/
@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
//    @RequestMapping(method = RequestMethod.GET)
//    public String listUsers(ModelMap model)
//    {
//        model.addAttribute("user", new User());
//        model.addAttribute("users", userRepository.findAll());
//        return "hello";
//    }


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


    @RequestMapping(  method = RequestMethod.GET)
    public
    @ResponseBody
    String listUsersJson(ModelMap model) {
        JsonArray userArray = new JsonArray();

        Gson gson=new Gson();
       List<User> userList= userRepository.findAll();

        for (User user : userList) {

            userArray.add( gson.toJsonTree(user));
        }

        userList= userRepository.findByFirstNameLike("dav%");
        for (User user : userList) {

            userArray.add( gson.toJsonTree(user));
        }

        return userArray.toString();
    }
}
