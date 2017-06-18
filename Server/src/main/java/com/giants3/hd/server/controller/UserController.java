package com.giants3.hd.server.controller;

import com.giants3.hd.utils.noEntity.BufferData;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.*;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
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
    private UserService userService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CustomerService customerService;

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
    GlobalDataService globalDataService;


    @Autowired
    private ProductService productService;
    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;

    @Autowired
    private WorkFlowService workFlowService;



    @Autowired
    private OrderAuthRepository orderAuthRepository;

    @Autowired
    private StockOutAuthRepository stockOutAuthRepository;

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
        return wrapData(userService.list());
    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users) {


        try {
              userService.saveUserList(users);
               return wrapData( userService.list());
        } catch (HdException e) {
            return wrapError(e.getMessage());
        }

    }


    @RequestMapping(value = "/initData", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<BufferData> initData(@RequestBody User user) {


        return getInitData(user.id);


    }


    /**
     * 提供移动端  省略很多数据
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getInitData", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<BufferData> getInitData(@RequestParam(value = "userId") long userId) {


        User user = userRepository.findOne(userId);

        if (user == null) {
            return wrapError("未找到用户");
        }
        //关闭密码返回
        user.password="";
        BufferData bufferData = new BufferData();
        bufferData.authorities = authorityRepository.findByUser_IdEquals(user.id);
        bufferData.customers = customerService.list();

        bufferData.materialClasses = materialClassRepository.findAll();
        bufferData.packMaterialClasses = packMaterialClassRepository.findAll();
        bufferData.packMaterialPositions = packMaterialPositionRepository.findAll();

        bufferData.materialTypes = materialTypeRepository.findAll();
        bufferData.packMaterialTypes = packMaterialTypeRepository.findAll();
        bufferData.packs = packRepository.findAll();
        bufferData.pClasses = productClassRepository.findAll();


        bufferData.salesmans = userService.getSalesman();;


        QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(user.id);
        if (quoteAuth == null) {
            quoteAuth = new QuoteAuth();
            quoteAuth.user = user;
        }
        bufferData.quoteAuth = quoteAuth;





        bufferData.workFlowSubTypes = workFlowService.getWorkFlowSubTypes().datas
        ;




        OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(user.id);
        if (orderAuth == null) {
            orderAuth = new OrderAuth();
            orderAuth.user = user;
        }
        bufferData.orderAuth = orderAuth;


        StockOutAuth stockOutAuth = stockOutAuthRepository.findFirstByUser_IdEquals(user.id);
        if (stockOutAuth == null) {
            stockOutAuth = new StockOutAuth();
            stockOutAuth.user = user;
        }
        bufferData.stockOutAuth = stockOutAuth;


        //读取第一条数据   总共就一条
        bufferData.globalData = globalDataService.findCurrentGlobalData();
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
                    productDetail = (ProductDetail) ObjectUtils.deepCopy(productDetail);
                    productDetail.swipe();
                    demos.add(productDetail);
                }

            }

        }
        bufferData.demos = demos;



        //读取流程节点工作人员
        bufferData.workFlowWorkers=workFlowService.getWorkFlowWorkers(user.id);


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
