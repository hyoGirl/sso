package com.sso.web;

import com.sso.common.interceptor.ServerLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(new ServerLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/ssoApi/authToken", "/logout", "/static/**","/templates/**");
    }
}
