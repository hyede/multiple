
package com.yede.multiple.common;


import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController extends BaseController {

    public ExceptionHandlerController() {
    }

    @ExceptionHandler
    @ResponseBody
    public <T extends Serializable> ResponseVO<T> handleException(Exception e, HttpServletResponse httpServletResponse) {
        if (e instanceof ApplicationException) {
            log.error("ApplicationException thrown: ", e);
            ApplicationException ex = (ApplicationException)e;
            return this.failedResponse(ex.getMessage(),ex.getErrorCode());
        } else {
            log.error("UnexpectedException thrown: ", e);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return this.failedResponse(ErrorCode.sys_error);
        }
    }
}
