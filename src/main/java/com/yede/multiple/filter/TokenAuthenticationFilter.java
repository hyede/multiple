package com.yede.multiple.filter;

import com.yede.multiple.common.ResponseVO;
import com.yede.multiple.exception.ErrorCode;
import com.yede.multiple.model.UserDetailsBean;
import com.yede.multiple.utils.AppSec;
import com.yede.multiple.utils.JsonMapper;
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

@Slf4j
public class TokenAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = req.getRequestURI();
        log.debug("Request url: " + url);
        UserDetailsBean userDetailsBean = (UserDetailsBean)req.getSession().getAttribute("user");
        if (userDetailsBean == null) {
            resp.setContentType("application/json");
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());

            ResponseVO responseVO = new ResponseVO<>();
            responseVO.setResponseTime(new Date());
            responseVO.setResponseStatus(ResponseVO.ResponseStatus.failed);
            responseVO.setErrorCode(ErrorCode.token_expired);
            responseVO.setErrorMsg(ErrorCode.token_expired.getErrorMessage());
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(JsonMapper.obj2String(responseVO));
            printWriter.flush();
            return;
        }
        AppSec.setLoginUser(userDetailsBean);
        AppSec.addRequest(req);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }
}
