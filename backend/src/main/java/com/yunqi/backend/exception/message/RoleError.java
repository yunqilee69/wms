package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;
import com.yunqi.backend.common.result.ResultCode;

/**
 * @author liyunqi
 */
public enum RoleError implements BaseError {
    ID_IS_EMPTY(20001, "角色id不能为空"),
    ;

    int code;
    String msg;

    RoleError(int code, String msg) {
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
