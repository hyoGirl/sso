package com.sso.api;

import com.sso.common.constants.ClientCache;
import com.sso.common.constants.SsoConstants;
import com.sso.common.model.LoginUser;
import com.sso.common.model.SSOTip;
import com.sso.util.MyTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenApIService {


    //token验证的url
    public static final String AUTH_TOKEN_URL = "/ssoApi/authToken";

    //清除局部
    public static final String CLEAR_TOKEN_URL = "/ssoApi/clearToken";

    @Autowired
    MyTokenUtil myTokenUtil;

    @RequestMapping(AUTH_TOKEN_URL)
    public SSOTip validateToken(HttpServletRequest request){


        //获取校验的token
        String token = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);
        String clientAddr = request.getParameter(SsoConstants.CLIENT_REQUEST_ADDR_PARAM_NAME);

        boolean flag = myTokenUtil.checkToken(request, token, clientAddr);

        if(flag){
            //根据token去获取这个登陆者
            LoginUser loginUser= myTokenUtil.getLoginUserByToken(token);

            return SSOTip.success(loginUser.getUserName());
        }else{
            return  SSOTip.error("token 校验失败!!!");
        }

    }



    @RequestMapping(AUTH_TOKEN_URL)
    public Map<String,Object> cleanToken(HttpServletRequest request){

        Map resultMap=new HashMap();

        //获取校验的token

        String tokenParam = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);


        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        ClientCache clientCache = applicationContext.getBean(ClientCache.class);

        //
        clientCache.addInvalidKey(tokenParam);


        resultMap.put("code",200);
        resultMap.put("msg","SUCCESS");


        return resultMap;

    }



}
