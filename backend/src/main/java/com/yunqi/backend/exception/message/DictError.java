package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */public enum DictError implements BaseError {
    PARAM_IS_EMPTY(30001, "请求参数为空，请确认后重试");

    int code;

    String msg;

    DictError(int code, String msg) {
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
