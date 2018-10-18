package com.sso.controller;

import com.sso.common.constants.SsoConstants;
import com.sso.util.MyTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {


    @Autowired
    RestTemplate restTemplate;


    @Autowired
    MyTokenUtil myTokenUtil;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request) {

        String result = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();

        System.out.println("GET 请求访问的地址为： "+result);

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            System.err.println("GET 方法的 session: "+cookie.getName()+": "+cookie.getValue());
        }

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        String result = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        System.out.println("POST 请求访问的地址为： "+result);

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        String redirectUrl = request.getParameter(SsoConstants.REDIRECT_PARAM_NAME);
        System.out.println("服务端获取了客户端访问的地址："+redirectUrl);


        HttpSession session = request.getSession();


        if (userName.equals("admin") && ("admin").equals(password)) {


            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {

                System.err.println("服务端接受到的 session: "+cookie.getName()+": "+cookie.getValue());
            }

            String token = myTokenUtil.createToken(userName,password);


            System.out.println("服务端登录时产生的token为： "+token);

            //login 为登录成功的标志
            session.setAttribute(SsoConstants.SESSION_LOGIN_FLAG, token);
            System.out.println("服务端设置登录成功的session ID：" + session.getId());

            try {

                System.out.println("服务端跳转客户端的地址为： "+redirectUrl + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + token);

                response.sendRedirect(redirectUrl + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + token);
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "login";

    }




    @RequestMapping(SsoConstants.LOGOUT_URL)
    public String logout(HttpServletRequest request, Model model) {


        HttpSession session = request.getSession();

        //销毁自己的session

        if (session != null) {
            session.invalidate();
        }

        //通知所有人，注销了


        String tokenParam = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);
        myTokenUtil.logOutAll(tokenParam);






        return null;

    }

}
