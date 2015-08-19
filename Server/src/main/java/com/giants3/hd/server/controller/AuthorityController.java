package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
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

    private SessionRepository sessionRepository;


    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AppVersionRepository appVersionRepository;

    @Autowired
    QuoteAuthRepository quoteAuthRepository;


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
                if(authority.module.id==module.id)
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
    RemoteData<User> login( HttpServletRequest request, @RequestBody User user)   {


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



        RemoteData<User>  data= wrapData(findUser);
        long loginTime=Calendar.getInstance().getTimeInMillis();
        data.token= DigestUtils.md5(findUser.toString()+loginTime);


       //Session session= sessionRepository.findFirstByUser_IdEqualsEqualsOrderByLoginTimeDesc(findUser.id);

        Session     session=new Session();
         session.user
                    =findUser;


        session.loginTime=loginTime;
        session.token= data.token;
        session.loginIp=request.getRemoteAddr();

        sessionRepository.save(session);


        return data;
    }

    @RequestMapping(value="/moduleList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Module> moduleList( )   {


        return    wrapData(moduleRepository.findAll());


    }


    @RequestMapping(value="/loadAppVersion", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<AppVersion> loadAppVersion( )   {


        AppVersion appVersion=appVersionRepository.findFirstByAppNameLikeOrderByVersionCodeDesc("%%");
        if(appVersion==null)
        {
            wrapError("无最新版本");
        }

        return    wrapData(appVersion);


    }




    @RequestMapping(value="/findQuoteAuth", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> findQuoteAuth(@RequestParam(value = "userId" ) long userId)   {


        QuoteAuth quoteAuth=quoteAuthRepository.findFirstByUser_IdEquals(userId);
        return    wrapData(quoteAuth);


    }


    @RequestMapping(value="/quoteAuthList", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuoteAuth> quoteAuthList( )   {



        List<User> users=userRepository.findAll();


        List<QuoteAuth> quoteAuths=quoteAuthRepository.findAll();
        List<QuoteAuth> unConfigAuthorities=new ArrayList<>();

        int size=users.size();
        for (int i = 0; i < size; i++) {

            User user=users.get(i);
            boolean found=false;
            for(QuoteAuth quoteAuth:quoteAuths)
            {
                if(user.id==quoteAuth.user.id)
                {
                    found=true;
                    break;
                }
            }

            if(!found)
            {
                QuoteAuth authority=new QuoteAuth();
                authority.user=user;
                unConfigAuthorities.add(authority);

            }


        }

        quoteAuths.addAll(unConfigAuthorities);



        return wrapData(quoteAuths);
    }

    @RequestMapping(value="/saveQuoteList", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<QuoteAuth> saveQuoteList(  @RequestBody List<QuoteAuth> authorities)   {



        List<QuoteAuth> newData=new ArrayList<>();

        for(QuoteAuth authority : authorities)
        {

            newData.add( quoteAuthRepository.save(authority));
        }


        return  wrapData(newData);
    }

}
