package com.yede.multiple.exception;

public enum ErrorCode {
    sys_error("system error"),
    token_expired("token_expired");

    private String errorMessage;

    private ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
