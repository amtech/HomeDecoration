package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.ProductService;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.*;


import com.giants3.hd.server.noEntity.BufferData;
import com.giants3.hd.server.noEntity.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/18.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

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
    private PackRepository packRepository;

    @Autowired
    private ProductClassRepository productClassRepository;

    @Autowired
    private MaterialClassRepository materialClassRepository;

    @Autowired
    private GlobalDataRepository globalDataRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;

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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list() {
        return wrapData(userRepository.findAll());
    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users) {

        for (User user : users) {


            User oldData = userRepository.findOne(user.id);
            if (oldData == null) {
                user.id = -1;


                //如果是空数据  略过添加
                if (user.isEmpty()) {
                    continue;
                }


            } else {


                user.id = oldData.id;

            }

            userRepository.save(user);


        }


        return list();
    }


    @RequestMapping(value = "/initData", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<BufferData> initData(@RequestBody User user) {


        BufferData bufferData = new BufferData();
        bufferData.authorities = authorityRepository.findByUser_IdEquals(user.id);
        bufferData.customers = customerRepository.findAll();

        bufferData.materialClasses = materialClassRepository.findAll();
        bufferData.packMaterialClasses = packMaterialClassRepository.findAll();
        bufferData.packMaterialPositions = packMaterialPositionRepository.findAll();

        bufferData.materialTypes = materialTypeRepository.findAll();
        bufferData.packMaterialTypes = packMaterialTypeRepository.findAll();
        bufferData.packs = packRepository.findAll();
        bufferData.pClasses = productClassRepository.findAll();
        bufferData.salesmans = userRepository.findByIsSalesman(true);
        QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(user.id);
        if (quoteAuth == null) {
            quoteAuth = new QuoteAuth();
            quoteAuth.user = user;
        }
        bufferData.quoteAuth = quoteAuth;


        //读取第一条数据   总共就一条
        bufferData.globalData = globalDataRepository.findAll().get(0);
        bufferData.factories = factoryRepository.findAll();
        List<ProductDetail> demos = new ArrayList<>();
        ProductDetail productDetail = null;
        RemoteData<Product> result = productService.searchProductList(ConstantData.DEMO_PRODUCT_NAME, 0, 100);
        if (result.isSuccess() && result.totalCount > 0) {

            int size = result.datas.size();
            for (int i = 0; i < size; i++) {
                productDetail = productService.findProductDetailById(result.datas.get(i).id);
                if (productDetail != null) {
                    //擦除去记录信息
                    productDetail= (ProductDetail)ObjectUtils.deepCopy(productDetail);
                    productDetail.swipe();
                    demos.add(productDetail);
                }

            }

        }
        bufferData.demos = demos;


        return wrapData(bufferData);
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> updatePassword(@RequestBody String[] map) {


        long userId = Long.valueOf(map[0]);
        String oldPassword = map[1];
        String newPassword = map[2];
        User user = userRepository.findOne(userId);
        if (user != null) {

            if (user.password.equals(oldPassword)) {

                user.password = newPassword;
                userRepository.save(user);
                return wrapData();
            } else {
                return wrapError("旧密码错误");
            }


        } else {

            return wrapError("要修改的员工不存在");
        }


    }
}
