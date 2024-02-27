package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;
import com.yunqi.backend.common.result.ResultCode;

/**
 * @author liyunqi
 */
public enum SystemError implements BaseError {
    PERMS_DENIED(ResultCode.UNAUTHORIZED, "权限不足，无法访问该系统资源"),

    ;

    int code;
    String msg;

    SystemError(int code, String msg) {
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
