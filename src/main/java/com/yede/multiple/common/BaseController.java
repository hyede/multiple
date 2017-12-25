package com.yede.multiple.common;


import com.yede.multiple.exception.ErrorCode;

import java.io.Serializable;

public abstract class BaseController {
    public BaseController() {
    }

    protected <T extends Serializable> ResponseVO<T> successResponse(T body) {
        ResponseVO resp = new ResponseVO();
        resp.setResponseStatus(ResponseVO.ResponseStatus.succeed);

        resp.setResponseBody(body);
        return resp;
    }

    protected <T extends Serializable> ResponseVO<T> successResponse() {
        ResponseVO resp = new ResponseVO();
        resp.setResponseStatus(ResponseVO.ResponseStatus.succeed);
        return resp;
    }

    protected <T extends Serializable> ResponseVO<T> failedResponse(ErrorCode errorCode) {
        ResponseVO resp = new ResponseVO();
        resp.setResponseStatus(ResponseVO.ResponseStatus.failed);
        resp.setErrorMsg(errorCode.getErrorMessage());
        resp.setErrorCode(errorCode);
        return resp;
    }

    protected <T extends Serializable> ResponseVO<T> failedResponse(String msg,ErrorCode errorCode) {
        ResponseVO resp = new ResponseVO();
        resp.setResponseStatus(ResponseVO.ResponseStatus.failed);
        resp.setErrorMsg(msg);
        resp.setErrorCode(errorCode);
        return resp;
    }
}