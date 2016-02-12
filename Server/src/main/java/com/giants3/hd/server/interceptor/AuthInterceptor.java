package com.giants3.hd.server.interceptor;


import com.giants3.hd.server.repository.SessionRepository;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.crypt.CryptUtils;
import com.giants3.hd.utils.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.logging.Logger;


public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static  final String TAG="AuthInterceptor";
    private static final CharSequence WEIXIN = "weixin";
    public static final String TOKEN = "token";
    public String UNLOGIN="api/authority/unLogin";

    public String UN_INTERCEPT_USERLIST="/api/user/list";
    public String UN_INTERCEPT_ALOGIN="/api/authority/aLogin";
    public String UN_INTERCEPT_LOGIN="/api/authority/login";

    public String UN_INTERCEPT_FILE="/api/file/download/";

    public long VALIDATE_TIME=24l*60*60*1000;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Logger.getLogger(TAG).info("AuthInterceptor" );
        String url=request.getRequestURI();
        HttpServletRequestWrapper wrapper;
        //非过滤的url
        if(url.contains(UN_INTERCEPT_LOGIN)||url.contains(UN_INTERCEPT_ALOGIN)||url.contains(UN_INTERCEPT_USERLIST)||url.contains(UNLOGIN)||url.contains(UN_INTERCEPT_FILE)||url.contains(WEIXIN))
            return true;

          else
            {
            String token=request.getParameter(TOKEN);
            Session session= sessionRepository.findFirstByTokenEquals(token);

            if(session!=null)
            {

              long currentTime=  Calendar.getInstance().getTimeInMillis();
                if(currentTime-session.loginTime<VALIDATE_TIME)
                {
                    request.setAttribute(Constraints.ATTR_LOGIN_USER,session.user);
                    return true;
                }




            }
               //如果验证失败
                    //返回到登录界面
                RemoteData<Void> data=new RemoteData<>();
                data.code=RemoteData.CODE_UNLOGIN;
                data.message="用户未登录，或者登录超时失效";


                if(ConstantData.IS_CRYPT_JSON)
                {

                    response.getOutputStream().write(  CryptUtils.encryptDES(GsonUtils.toJson(data ).getBytes(GsonUtils.UTF_8), ConstantData.DES_KEY) );
                }else
                {

                    response.getOutputStream().write(GsonUtils.toJson(data ).getBytes(GsonUtils.UTF_8));
                }

                 return false;


            }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}