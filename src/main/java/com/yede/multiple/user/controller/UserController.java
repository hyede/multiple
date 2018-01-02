package com.yede.multiple.user.controller;


import com.yede.multiple.common.BaseController;
import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.exception.ErrorCode;
import com.yede.multiple.model.UserDetailsBean;
import com.yede.multiple.user.entity.SysUser;
import com.yede.multiple.user.entity.bean.UserLoginBean;
import com.yede.multiple.user.service.SysUserService;
import com.yede.multiple.user.service.TokenService;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.DatabaseContextHolder;
import com.yede.multiple.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController{
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseVO logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
//        String path = "signin.jsp";
//        response.sendRedirect(path);
         return    this.successResponse();
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseVO<UserDetailsBean> login(@RequestBody UserLoginBean loginBean, HttpServletRequest request, HttpServletResponse response) throws Exception {
         String organizationCode = loginBean.getOrganizationCode();
        DatabaseContextHolder.setDataSourceKey(organizationCode);
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();

        SysUser sysUser = sysUserService.findByUserName(username);
        String errorMsg = "";

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else if (sysUser == null) {
            errorMsg = "查询不到指定的用户";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
        } else {
            // login success
            UserDetailsBean userDetailsBean= UserDetailsBean.builder().userId(Long.valueOf(sysUser.getId())).userName(sysUser.getUsername()).fullName(sysUser.getUsername()).organizationCode(organizationCode).build();
            request.getSession().setAttribute("user", userDetailsBean);

            AppSec.setLoginUser(userDetailsBean);
            //判断如果是mobile，则调用tokenService.generateToken(userDetailsBean, true)
            String token;
            if (TokenService.MOBILE_TYPE.equalsIgnoreCase(request.getHeader(TokenService.USER_AGENT_HEADER))){
                token=tokenService.generateToken(request.getRemoteAddr(), userDetailsBean, true);
            }else {
                token = tokenService.generateToken(request.getRemoteAddr(), userDetailsBean);
            }
            response.setHeader(TokenService.SET_TOKEN_HEADER, token);
            return this.successResponse(userDetailsBean);
        }
        throw new ApplicationException(errorMsg,ErrorCode.sys_error);

    }
}
