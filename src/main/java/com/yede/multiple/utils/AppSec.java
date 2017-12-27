package com.yede.multiple.utils;

import com.yede.multiple.model.UserDetailsBean;

import javax.servlet.http.HttpServletRequest;

public class AppSec {

    private static final ThreadLocal<UserDetailsBean> loginUser = new ThreadLocal();
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static UserDetailsBean getLoginUser() {
        return loginUser.get();
    }

    public static void setLoginUser(UserDetailsBean userDetailsBean) {
        loginUser.set(userDetailsBean);
    }

    public static void clearLoginUser() {
        loginUser.remove();
    }

    public static void addRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        loginUser.remove();
        requestHolder.remove();
    }
}