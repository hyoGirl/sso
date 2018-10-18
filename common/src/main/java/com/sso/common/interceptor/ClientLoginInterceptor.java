package com.sso.common.interceptor;

import com.sso.common.api.ApiService;
import com.sso.common.util.HttpUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientLoginInterceptor implements HandlerInterceptor {


    /**
     *  两者session的id不一样
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        //获取session中登录成功标志
        HttpSession session = httpServletRequest.getSession();

        System.out.println("客户端拦截器上获取到的session ID: "+session.getId());

        Object loginFlag = session.getAttribute("login");


        System.out.println("客户端拦截器上获取到的登录成功标志为： "+loginFlag);

        //检验是否存在登录成功标志
        if(loginFlag==null){


            String token = httpServletRequest.getParameter("token");

            System.out.println("客户端拦截器上获取到的token： "+token);

             //不存在，就校验token
            if(token!=null){
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
                ApiService apiService = webApplicationContext.getBean(ApiService.class);


                boolean validateToken = apiService.validateToken(token);

                if(validateToken){
                    //token正确，就在session中增加登录成功标志
                    session.setAttribute("login",token);
                    System.out.println("客户端拦截器上设置登录成功标志的session ID为："+session.getId());
                    return true;
                }else{
                    remoteLogin(httpServletRequest,httpServletResponse);
                    return false;
                }
            }else{
                remoteLogin(httpServletRequest,httpServletResponse);
                return false;
            }
        }else {
            //如果·存在登录成功标志，
            return true;
        }
    }


    private void remoteLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {


        String path= HttpUtil.encodeUrl(HttpUtil.getRequestFullPathNoParam(httpServletRequest));

        try {

            System.out.println("客户端浏览器上输入的url为： "+path);

            System.out.println("客户端拦截器向服务端跳转的url为： "+"http://localhost:8554/sso/"+"?"+"redirectUrl="+path);

            httpServletResponse.sendRedirect("http://localhost:8554/sso/"+"?"+"redirectUrl="+path);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
