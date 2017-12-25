
package com.yede.multiple.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yede.multiple.exception.ErrorCode;

import java.io.Serializable;
import java.util.Date;

public class ResponseVO<T extends Serializable> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Date responseTime;
    private ResponseVO.ResponseStatus responseStatus;
    private T responseBody;
    private String errorMsg;
    private ErrorCode errorCode;
    private String exception;

    public ResponseVO() {
    }

    public Date getResponseTime() {
        return this.responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public ResponseVO.ResponseStatus getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(ResponseVO.ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getResponseBody() {
        return this.responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getException() {
        return this.exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException var2) {
            return null;
        }
    }

    public static enum ResponseStatus {
        succeed,
        failed;

        private ResponseStatus() {
        }
    }
}
