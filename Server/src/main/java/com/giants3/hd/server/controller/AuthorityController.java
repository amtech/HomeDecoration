package com.giants3.hd.server.controller;

import com.giants3.hd.appdata.AUser;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.UserService;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 权限
 * Created by davidleen29 on 2014/9/18.
 */
@Controller

@RequestMapping("/authority")
public class AuthorityController extends BaseController {


    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired

    private SessionRepository sessionRepository;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    QuoteAuthRepository quoteAuthRepository;

    @Autowired
    StockOutAuthRepository stockOutAuthRepository;

    @Autowired
    OrderAuthRepository orderAuthRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    @Qualifier("CustomImplName")
    DataParser<User, AUser> dataParser;

    @RequestMapping(value = "/findByUser", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Authority> findByUser(@RequestParam(value = "userId") long userId) {


        User user = userRepository.findOne(userId);

        List<Module> modules = moduleRepository.findAll();
        List<Authority> authorities = authorityRepository.findByUser_IdEquals(userId);
        List<Authority> unConfigAuthorities = new ArrayList<>();
        int moduleSize = modules.size();
        for (int i = 0; i < moduleSize; i++) {

            Module module = modules.get(i);
            boolean found = false;
            for (Authority authority : authorities) {
                if (authority.module.id == module.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Authority authority = new Authority();
                authority.module = module;
                authority.user = user;
                unConfigAuthorities.add(authority);

            }


        }

        authorities.addAll(unConfigAuthorities);


        return wrapData(authorities);


    }


    @RequestMapping(value = "/saveList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Authority> save(@RequestParam(value = "userId") long userId, @RequestBody List<Authority> authorities) {


        User user = userRepository.findOne(userId);
        List<Authority> newData = new ArrayList<>();

        for (Authority authority : authorities) {
            authority.user = user;

            Authority findSameAuthority = authorityRepository.findFirstByUser_IdEqualsAndModule_IdEquals(authority.user.id, authority.module.id);

            //保证数据新增或者修改 不存在重复增加
            if (findSameAuthority == null) {
                authority.id = -1;
            } else {
                authority.id = findSameAuthority.id;
            }

            newData.add(authorityRepository.save(authority));
        }


        return wrapData(newData);
    }



    /**
     * 提供给移动端调用的接口
     *
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/aLogin2", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<AUser> aLogin2(HttpServletRequest request, @RequestBody Map<String, String> params) {


        String userName = params.get("userName");
        String password = params.get("password");
        String client = params.get("client");
        int version = 0;
        try {
            version = Integer.valueOf(params.get("version"));
        } catch (Throwable t) {
        }

        String device_token=params.get("device_token");
        device_token= StringUtils.isEmpty(device_token)?"":device_token;
        String  versionName=params.get("versionName");
        versionName= StringUtils.isEmpty(versionName)?"":versionName;


        RemoteData<User> userRemoteData = doLogin2(userName, password, client, version, request.getRemoteAddr(),device_token);
        RemoteData<AUser> result = RemoteDataParser.parse(userRemoteData, dataParser);


        if (result.isSuccess()) {

            AUser loginUser = result.datas.get(0);
            loginUser.token = result.token;
            List<Authority> authorities = authorityRepository.findByUser_IdEquals(loginUser.id);
            QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(loginUser.id);
            loginUser.authorities = authorities;
            loginUser.quoteAuth = quoteAuth;
        }


        return result;


    }


    /**
     * 登录接口2  密码加密
     *
     * @param request
     * @param user
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<User> login2(HttpServletRequest request, @RequestBody User user, @RequestParam(value = "appVersion", required = false, defaultValue = "0") int appVersion, @RequestParam(value = "client", required = false, defaultValue = "DESKTOP") String client) {

        return doLogin2(user.name, user.password, client, appVersion, request.getRemoteAddr());
    }

    private RemoteData<User> doLogin2(String userName, String passwordMd5, String client, int version, String loginIp)
    {


       return doLogin2(userName,passwordMd5,client,version,loginIp,"" );
    }
    /**
     * 登录逻辑
     *
     * @param userName
     * @param passwordMd5
     * @param client
     * @param version
     * @param loginIp
     * @return
     */
    private RemoteData<User> doLogin2(String userName, String passwordMd5, String client, int version, String loginIp,String device_token) {


        List<User> userList = userRepository.findByNameEquals(userName);

        int size = userList.size();
        if (size <= 0)
            return wrapError("用户账户不存在");
        if (size > 1)
            return wrapError("存在重名用户，请联系管理员");

        User findUser = userList.get(0);



        if (! findUser.isCorrectPassword(passwordMd5)) {
            return wrapError("密码错误");
        }


        findUser.password = "***";

        RemoteData<User> data = wrapData(findUser);
        Date date=Calendar.getInstance().getTime();
        long loginTime =date.getTime();
        data.token = DigestUtils.md5(findUser.toString() + loginTime);
        AppVersion appVersion = getAppVersion();
        if (version>0&& appVersion != null && appVersion.versionCode > version) {
            data.newVersionCode = appVersion.versionCode;
            data.newVersionName = appVersion.versionName;
        }


        //Session session= sessionRepository.findFirstByUser_IdEqualsEqualsOrderByLoginTimeDesc(findUser.id);

        Session session = new Session();
        session.user
                = findUser;

        session.loginTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS.format(date);
        session.loginTime = loginTime;
        session.token = data.token;
        session.loginIp = loginIp;
        session.client=client;
        session.device_token=device_token;
        sessionRepository.save(session);


        return data;
    }


    @Deprecated
    private RemoteData<User> doLogin(HttpServletRequest request, String userName, String password, String client, String version) {


        List<User> userList = userRepository.findByNameEquals(userName);

        int size = userList.size();
        if (size <= 0)
            return wrapError("用户账户不存在");
        if (size > 1)
            return wrapError("存在重名用户，请联系管理员");

        User findUser = userList.get(0);
        if (!findUser.password.equals(password)) {
            return wrapError("密码错误");
        }


        findUser.password = "***";

        RemoteData<User> data = wrapData(findUser);
       Date date= Calendar.getInstance().getTime();
        long loginTime =date.getTime();
        data.token = DigestUtils.md5(findUser.toString() + loginTime);


        //Session session= sessionRepository.findFirstByUser_IdEqualsEqualsOrderByLoginTimeDesc(findUser.id);

        Session session = new Session();
        session.user
                = findUser;


        session.loginTime = loginTime;
        session.loginTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS.format(date);
        session.token = data.token;
        session.loginIp = request.getRemoteAddr();

        sessionRepository.save(session);


        return data;
    }

    @RequestMapping(value = "/moduleList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Module> moduleList() {


        return wrapData(moduleRepository.findAll());


    }


    @RequestMapping(value = "/loadAppVersion", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<AppVersion> loadAppVersion() {


        AppVersion appVersion = getAppVersion();
        if (appVersion == null) {
            wrapError("无最新版本");
        }

        return wrapData(appVersion);


    }

    private AppVersion getAppVersion() {
        return appVersionRepository.findFirstByAppNameLikeOrderByVersionCodeDescUpdateTimeDesc("%%");
    }


    @RequestMapping(value = "/findQuoteAuth", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> findQuoteAuth(@RequestParam(value = "userId") long userId) {


        QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(userId);
        return wrapData(quoteAuth);


    }


    @RequestMapping(value = "/quoteAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> quoteAuthList() {


        List<User> users = userService.list();


        List<QuoteAuth> quoteAuths = quoteAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<QuoteAuth> tempQuoteAuthList = new ArrayList<>();
        for (QuoteAuth quoteAuth : quoteAuths) {
            if (quoteAuth.user.deleted) tempQuoteAuthList.add(quoteAuth);
        }
        quoteAuths.removeAll(tempQuoteAuthList);


        tempQuoteAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (QuoteAuth quoteAuth : quoteAuths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                QuoteAuth authority = new QuoteAuth();
                authority.user = user;
                tempQuoteAuthList.add(authority);

            }


        }

        quoteAuths.addAll(tempQuoteAuthList);


        return wrapData(quoteAuths);
    }


    @RequestMapping(value = "/orderAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OrderAuth> orderAuthList() {


        List<User> users = userService.list();


        List<OrderAuth> auths = orderAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<OrderAuth> tempAuthList = new ArrayList<>();
        for (OrderAuth aAuth : auths) {
            if (aAuth.user.deleted) tempAuthList.add(aAuth);
        }
        auths.removeAll(tempAuthList);


        tempAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (OrderAuth quoteAuth : auths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                OrderAuth authority = new OrderAuth();
                authority.user = user;
                tempAuthList.add(authority);

            }


        }

        auths.addAll(tempAuthList);


        return wrapData(auths);
    }


    @RequestMapping(value = "/stockOutAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<StockOutAuth> stockOutAuthList() {


        List<User> users = userService.list();


        List<StockOutAuth> auths = stockOutAuthRepository.findAll();
        //移除user 为delete 的权限配置
        List<StockOutAuth> tempAuthList = new ArrayList<>();
        for (StockOutAuth aAuth : auths) {
            if (aAuth.user.deleted) tempAuthList.add(aAuth);
        }
        auths.removeAll(tempAuthList);


        tempAuthList.clear();


        int size = users.size();
        for (int i = 0; i < size; i++) {

            User user = users.get(i);
            if (user.deleted) continue;
            boolean found = false;
            for (StockOutAuth quoteAuth : auths) {
                if (user.id == quoteAuth.user.id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                StockOutAuth authority = new StockOutAuth();
                authority.user = user;
                tempAuthList.add(authority);

            }


        }

        auths.addAll(tempAuthList);


        return wrapData(auths);
    }

    @RequestMapping(value = "/saveQuoteList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<QuoteAuth> saveQuoteList(@RequestBody List<QuoteAuth> authorities) {


        List<QuoteAuth> newData = new ArrayList<>();

        for (QuoteAuth authority : authorities) {

            newData.add(quoteAuthRepository.save(authority));
        }


        return wrapData(newData);
    }


    @RequestMapping(value = "/saveStockOutList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<StockOutAuth> saveStockOutList(@RequestBody List<StockOutAuth> authorities) {


        List<StockOutAuth> newData = new ArrayList<>();

        for (StockOutAuth authority : authorities) {

            newData.add(stockOutAuthRepository.save(authority));
        }


        return wrapData(newData);
    }


    @RequestMapping(value = "/saveOrderList", method = RequestMethod.POST)
    public
    @ResponseBody
    @Transactional
    RemoteData<OrderAuth> saveOrderList(@RequestBody List<OrderAuth> authorities) {


        List<OrderAuth> newData = new ArrayList<>();

        for (OrderAuth authority : authorities) {

            newData.add(orderAuthRepository.save(authority));
        }


        return wrapData(newData);
    }

}
