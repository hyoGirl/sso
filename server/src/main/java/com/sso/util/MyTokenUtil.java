package com.sso.util;

import com.sso.api.TokenApIService;
import com.sso.common.constants.SsoConstants;
import com.sso.common.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MyTokenUtil {


    /**
     * 用户和token保存的数据表
     */
    private Map<String,Object> tokenMap=new ConcurrentHashMap<String,Object>();

    /**
     *  已经登录成功的站点表信息
     */
    private Set<String> ssoClients = new HashSet<>();


    @Autowired
    private RestTemplate restTemplate;


    public  String createToken(String userName,String password){

        String token = UUID.randomUUID().toString();

        //模拟数据库
        LoginUser loginUser=new LoginUser();
        loginUser.setUserName(userName);
        loginUser.setPassword(password);

        tokenMap.put(token,loginUser);

        return token;

    }

    public boolean checkToken(HttpServletRequest request, String token, String clientAddr){
        if(tokenMap.containsKey(token)){
            //如果token正确，就把该站点放到这个表中
            ssoClients.add(clientAddr);
            return true;
        }else{
            return false;
        }
    }

    public void logOutAll(String tokenParam) {


        //从所有的   站点信息中删除掉,同时还删除用户信息


        for(String ssoClient: ssoClients){

            //  获取所有的站点信息。每一个站点都去注销这个信息
            String  path=ssoClient+ TokenApIService.CLEAR_TOKEN_URL+"?"+ SsoConstants.TOKEN_PARAM_NAME+"="+tokenParam;

            try {
                restTemplate.postForObject(path, null, Map.class);
            } catch (RestClientException e) {
                System.out.println("发送注销请求失败!!!");
            }
        }

    }

    public LoginUser getLoginUserByToken(String token) {

        return  (LoginUser) tokenMap.get(token);

    }
}
