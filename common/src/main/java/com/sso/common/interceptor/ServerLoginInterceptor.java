package com.sso.common.interceptor;

import com.sso.common.constants.SsoConstants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServerLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String result = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getRequestURI();

        System.err.println("客户端请求访问的地址为： "+result);






        HttpSession session = httpServletRequest.getSession();

        Cookie[] cookies = httpServletRequest.getCookies();

        for (Cookie cookie : cookies) {

            System.err.println("服务端拦截器获取到的session: "+cookie.getName()+": "+cookie.getValue());

        }

        System.err.println("服务端拦截器获取的session id "+session.getId());

        //服务端获取当前用户是否登录。
        Object sessionAttribute = session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);

        System.out.println("服务端拦截器获取到的登录成功标志为： "+sessionAttribute);


        String redirectUrl = httpServletRequest.getParameter(SsoConstants.REDIRECT_PARAM_NAME);

        System.out.println("客户端传递过来跳转的url为： "+redirectUrl);

        //如果没登录,跳转到登录页面
        if(sessionAttribute==null){
            httpServletRequest.setAttribute(SsoConstants.REDIRECT_PARAM_NAME,redirectUrl);
            httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest,httpServletResponse);
            return  false;
        }else{
            httpServletResponse.sendRedirect(redirectUrl  + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + sessionAttribute.toString());
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
