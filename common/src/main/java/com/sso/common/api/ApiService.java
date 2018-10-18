package com.sso.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiService {


    private static String validateTokenUrl = "/sso/validateToken/";


    @Autowired
    RestTemplate restTemplate;

    public boolean validateToken(String token) {

        String url = "http://localhost:8554" + validateTokenUrl + "?" + "token=" + token;

        Map<String, Object> data = restTemplate.postForObject(url, null, Map.class);

        if (data != null) {
            if (data.get("code").equals(200)) {
                return true;
            }
        }
        return false;
    }


}
