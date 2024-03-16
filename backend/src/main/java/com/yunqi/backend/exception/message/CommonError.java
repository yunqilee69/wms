package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum CommonError implements BaseError {

    PHONE_ERROR(11001, "手机号格式错误"),
    PHONE_REPEAT(11002, "手机号已存在");

    int code;

    String msg;

    CommonError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
