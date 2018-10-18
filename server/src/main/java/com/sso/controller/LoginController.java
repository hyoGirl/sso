package com.sso.controller;

import com.sso.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
    TokenUtil tokenUtil;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request, ModelMap map) {
        String redirectUrl = request.getParameter("redirectUrl");
        System.out.println(redirectUrl);

        map.addAttribute("redirectUrl", redirectUrl);


        Cookie[] cookies = request.getCookies();

        System.out.println("----进入login页面的cookie----");
        for (Cookie cookie : cookies) {

            System.err.println(cookie.getName()+": "+cookie.getValue());
        }
        System.out.println("----进入login页面的cookie----");

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, HttpServletResponse response, Model model) {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String redirectUrl = request.getParameter("redirectUrl");


        System.out.println(redirectUrl);


        Cookie[] cookies = request.getCookies();

        System.out.println("----服务端接受到的cookie----");
        for (Cookie cookie : cookies) {

            System.err.println(cookie.getName()+": "+cookie.getValue());
        }
        System.out.println("----服务端接受到的cookie----");


        HttpSession session = request.getSession();


        if (userName.equals("admin") && ("admin").equals(password)) {

            String token = tokenUtil.createToken(userName);

            session.setAttribute("login", token);

            System.out.println("服务端登录时产生的token为： "+token);

            System.out.println("服务端设置登录成功的session ID：" + session.getId());

            try {
                System.out.println("服务端向客户端跳转的地址为： "+redirectUrl + "?" + "token" + "=" + token);

                response.sendRedirect(redirectUrl + "?" + "token" + "=" + token);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "login";

    }

}
