package com.yede.multiple.config;

import com.google.common.collect.Lists;
import com.yede.multiple.filter.AclControlFilter;
import com.yede.multiple.filter.TokenAuthenticationFilter;
import com.yede.multiple.interceptor.HttpInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).excludePathPatterns("/users/login").addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean tokenAuthenticationFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();
        registrationBean.setFilter(tokenAuthenticationFilter);
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean aclControlFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AclControlFilter aclControlFilter = new AclControlFilter();
        registrationBean.setFilter(aclControlFilter);
        registrationBean.setOrder(3);
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("exclusionUrls","/sysUsers/noAuth,/users/login");
        registrationBean.setInitParameters(initParameters);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}