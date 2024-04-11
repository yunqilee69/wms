package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum LocationError implements BaseError {
    LOCATION_IS_USING(90001, "货位正在使用，无法删除");
    private int code;
    private String msg;

    LocationError(int code, String msg) {
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
