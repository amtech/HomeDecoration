package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.*;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.BufferData;
import com.giants3.hd.noEntity.MessageInfo;
import com.giants3.hd.noEntity.ProductDetail;
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
    private ErpService erpService;


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
    public String addUser(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, BindingResult result) {

        userRepository.save(user);

        return "redirect:/";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<User> list(

            @RequestParam(value = "type",required = false,defaultValue = "-1") int userType
    ) {

        if(userType==-1)

        return wrapData(userService.list());

        return wrapData(userService.getSalesman());
    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)

    public
    @ResponseBody
    RemoteData<User> saveList(@RequestBody List<User> users) {


        try {
            userService.saveUserList(users);
            return wrapData(userService.list());
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
    @RequestMapping(value = "/getAppInitData", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<BufferData> getAppInitData(@RequestParam(value = "userId") long userId) {


        return getConfigData(userId,false);

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


        return getConfigData(userId,true);

    }




    private RemoteData<BufferData> getConfigData(long userId,boolean fromDesk)
    {

        User user = userRepository.findOne(userId);

        if (user == null) {
            return wrapError("未找到用户");
        }

        BufferData bufferData = new BufferData();


        bufferData.packMaterialClasses = packMaterialClassRepository.findAll();
        bufferData.packMaterialPositions = packMaterialPositionRepository.findAll();
        bufferData.packMaterialTypes = packMaterialTypeRepository.findAll();












        QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(user.id);
        if (quoteAuth == null) {
            quoteAuth = new QuoteAuth();
            quoteAuth.user = user;
        }
        bufferData.quoteAuth = quoteAuth;

        bufferData.packs = packRepository.findAll();


        //读取第一条数据   总共就一条
        bufferData.globalData = globalDataService.findCurrentGlobalData();

        if(fromDesk) {
            bufferData.customers = customerService.list();

            bufferData.workFlowSubTypes = workFlowService.getWorkFlowSubTypes().datas ;


            bufferData.materialClasses = materialClassRepository.findAll();
            bufferData.materialTypes = materialTypeRepository.findAll();

            bufferData.pClasses = productClassRepository.findAll();



            bufferData.authorities = authorityRepository.findByUser_IdEquals(user.id);

            bufferData.salesmans = userService.getSalesman();
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

        }

        //读取流程节点工作人员
        bufferData.workFlowWorkers = workFlowService.getWorkFlowWorkers(user.id);



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

            if (user.password.equals(oldPassword)|| DigestUtils.md5(oldPassword).equalsIgnoreCase(user.passwordMD5)) {

                user.password = newPassword;
                user.passwordMD5= DigestUtils.md5(newPassword);
                userRepository.save(user);
                return wrapData();
            } else {
                return wrapError("旧密码错误");
            }


        } else {

            return wrapError("要修改的员工不存在");
        }


    }

    @RequestMapping(value = "/updatePassword2", method = RequestMethod.GET)

    public
    @ResponseBody
    RemoteData<Void> updatePassword2(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,
            @RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword)

     {

        return userService.updatePassword(user,oldPassword,newPassword);
    }


    @RequestMapping(value = "/newMessage", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MessageInfo> newMessage(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user) {


        int count = erpService.getUnHandleWorkFlowMessageCount(user);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.newWorkFlowMessageCount = count;
        return wrapData(messageInfo);

    }

}
