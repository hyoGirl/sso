package com.sso;

import com.sso.common.constants.SsoConstants;
import com.sso.common.util.HttpUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class ClientController {


    @Autowired
    RestTemplate restTemplate;


    @RequestMapping("")
    public String toIndex(){
        return "hello";
    }


    /**
     * 开启退出
     * @return
     */
    @RequestMapping(SsoConstants.LOGOUT_URL)
    public String doLogout(HttpServletRequest request, HttpServletResponse response){


        HttpSession session = request.getSession();

        //直接传递到服务端去注销

        String ssourl="http://localhost:8554/sso/";

        String requestContextPath = HttpUtil.getRequestContextPath(request);

        System.out.println("站点地址： "+requestContextPath);

        //登陆成功的站点  + token(其实就是登陆成功的值)
        String path=ssourl+"/logout"+"?"+SsoConstants.REDIRECT_PARAM_NAME+"="+requestContextPath
                +"&"+SsoConstants.TOKEN_PARAM_NAME+"="+session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            System.out.println("注销失败");
        }

        return null;

    }

}
