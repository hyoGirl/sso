package com.sso.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServerLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        HttpSession session = httpServletRequest.getSession();

        System.out.println("服务端拦截器获取的session id "+session.getId());

        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println("----------------------");
        for (Cookie cookie : cookies) {
            System.err.println(cookie.getName()+": "+cookie.getValue());
        }
        System.out.println("----------------------");


        //服务端获取当前用户是否登录。
        Object loginFlag = session.getAttribute("login");

        System.out.println("服务端拦截器获取到的登录成功标志为： "+loginFlag);


        String redirectUrl = (String) httpServletRequest.getParameter("redirectUrl");

        System.out.println("服务端获取跳转的url为： "+redirectUrl);

        if(loginFlag==null){
            httpServletRequest.setAttribute("redirectUrl",redirectUrl);
            httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest,httpServletResponse);
            return  false;
        }else{
            httpServletResponse.sendRedirect(redirectUrl + "token" + "=" + loginFlag);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
