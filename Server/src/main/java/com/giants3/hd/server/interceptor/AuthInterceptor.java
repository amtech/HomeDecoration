package com.giants3.hd.server.interceptor;



        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

        import com.giants3.hd.server.repository.SessionRepository;
        import com.giants3.hd.server.repository.UserRepository;
        import com.giants3.hd.utils.RemoteData;
        import com.giants3.hd.utils.entity.Session;
        import com.google.gson.Gson;


        import com.sun.org.apache.xml.internal.security.c14n.implementations.UtfHelpper;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.method.HandlerMethod;
        import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
        import sun.util.resources.CalendarData;

        import java.util.Calendar;


public class AuthInterceptor extends HandlerInterceptorAdapter {


    public String UNLOGIN="api/authority/unLogin";

    public String UN_INTERCEPT_USERLIST="/api/user/list";

    public String UN_INTERCEPT_LOGIN="/api/authority/login";

    public long VALIDATE_TIME=24l*60*60*1000;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url=request.getRequestURI();
        //非过滤的url
        if(url.contains(UN_INTERCEPT_LOGIN)||url.contains(UN_INTERCEPT_USERLIST)||url.contains(UNLOGIN))
            return true;

          else
            {
            String token=request.getParameter("token");
            Session session= sessionRepository.findFirstByTokenEquals(token);

            if(session!=null)
            {

              long currentTime=  Calendar.getInstance().getTimeInMillis();
                if(currentTime-session.loginTime<VALIDATE_TIME)
                {
                    return true;
                }



            }
               //如果验证失败
                    //返回到登录界面
                RemoteData<Void> data=new RemoteData<>();
                data.code=RemoteData.CODE_UNLOGIN;
                data.message="update client to newest version.";
                response.getWriter().print(new Gson().toJson(data));
                 return false;


            }

    }
}