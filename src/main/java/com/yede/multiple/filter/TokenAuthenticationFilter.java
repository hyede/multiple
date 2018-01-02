package com.yede.multiple.filter;

import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ErrorCode;
import com.yede.multiple.model.UserDetailsBean;
import com.yede.multiple.user.service.TokenService;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.DatabaseContextHolder;
import com.yede.multiple.utils.DateUtils;
import com.yede.multiple.utils.JsonMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        log.debug("Request url: " + url);

        if(url.endsWith("/users/login")){
            //登陆接口不校验token，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        UserDetailsBean userDetailsBean = (UserDetailsBean)request.getSession().getAttribute("user");






        if (userDetailsBean == null) {

            String token=request.getHeader(TokenService.TOKEN_HEADER);
            Claims claims=null;
            try {
                claims = TokenService.parseToken(token);
                String organizationCode = (String) claims.get(TokenService.ORGANIZATION_CODE_KEY);
                String userName = (String) claims.get(TokenService.USER_NAME_KEY);
                String password = (String) claims.get(TokenService.PASSWORD_KEY);
                Long userId=Long.valueOf(claims.get(TokenService.USER_ID).toString());
                userDetailsBean=UserDetailsBean.builder().userId(userId).userName(userName).organizationCode(organizationCode).build();
                response.setHeader(TokenService.TOKEN_HEADER,token);
            }catch (Exception e) {
                log.error(e.toString());
            }
            if (userDetailsBean == null) {
                response.setContentType("application/json");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                ResponseVO responseVO = new ResponseVO<>();
                responseVO.setResponseTime(new Date());
                responseVO.setResponseStatus(ResponseVO.ResponseStatus.failed);
                responseVO.setErrorCode(ErrorCode.token_expired);
                responseVO.setErrorMsg(ErrorCode.token_expired.getErrorMessage());
                PrintWriter printWriter = response.getWriter();
                printWriter.print(JsonMapper.obj2String(responseVO));
                printWriter.flush();
                return;
            }

        }

        AppSec.setLoginUser(userDetailsBean);

        DatabaseContextHolder.setDataSourceKey(userDetailsBean.getOrganizationCode());

        AppSec.addRequest(request);

        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }
}
