package com.sso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {


    @RequestMapping("")
    public String toIndex(){
        return "hello";
    }
}
