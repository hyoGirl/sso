package com.sso.api;

import com.sso.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenApI {


    @Autowired
    TokenUtil tokenUtil;

    @PostMapping("/sso/validateToken")
    public Map<String,Object> validateToken(HttpServletRequest request){

        Map resultMap=new HashMap();
        String token = request.getParameter("token");
        boolean flag = tokenUtil.checkToken(token);
        if(flag){
            resultMap.put("code",200);
            resultMap.put("msg","SUCCESS");
        }else{
            resultMap.put("code",500);
            resultMap.put("msg","ERROR");
        }
        return resultMap;

    }
}
