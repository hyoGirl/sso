package com.sso.util;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenUtil {


    private Map<String,Object> tokenMap=new ConcurrentHashMap<String,Object>();

    public  String createToken(String userName){

        String token = UUID.randomUUID().toString();
        tokenMap.put(token,userName);
        return token;

    }

    public boolean checkToken(String token){
        if(tokenMap.containsKey(token)){
            return true;
        }else{
            return false;
        }
    }

}
