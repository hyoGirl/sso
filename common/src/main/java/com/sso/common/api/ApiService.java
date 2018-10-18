package com.sso.common.api;

import com.sso.common.constants.SsoConstants;
import com.sso.common.model.SSOTip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiService {


    private static String validateTokenUrl = "/ssoApi/authToken";


    public  static Integer RESPONSE_CODE=200;


    @Autowired
    RestTemplate restTemplate;

    public String validateToken(String token, String clientAddr) {

        String ssourl="http://localhost:8554/sso";
        String url = ssourl + validateTokenUrl + "?" + SsoConstants.TOKEN_PARAM_NAME + "="+ token+ "&" + SsoConstants.CLIENT_REQUEST_ADDR_PARAM_NAME + "=" + clientAddr;

        //HTTP请求发送去调用sso服务端的校验代码
        SSOTip data = restTemplate.postForObject(url, null, SSOTip.class);

        if (data != null) {

            if(data.getCode().equals(RESPONSE_CODE)){

                return data.getMsg();
            }
        }
        return null;
    }







}
