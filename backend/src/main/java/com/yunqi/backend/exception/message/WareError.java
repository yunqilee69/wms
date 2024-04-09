package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */public enum WareError implements BaseError {
    BAR_CODE_REPEAT(30003, "条形码重复"),
    PARAMS_EMPTY(30004, "请检查字段后重试"),
    WARE_IS_USING_IN_RECORD(30005, "货物正在被库存记录所使用");

    int code;

    String msg;

    WareError(int code, String msg) {
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
