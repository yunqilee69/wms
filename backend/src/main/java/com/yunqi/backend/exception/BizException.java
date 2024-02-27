package com.yunqi.backend.exception;

import com.yunqi.backend.common.base.BaseError;

/**
 * 业务异常
 * @author liyunqi
 */
public class BizException extends RuntimeException {

    private int code;

    private String msg;

    public BizException(BaseError baseError) {
        super(baseError.getErrorMsg());
        this.code = baseError.getCode();
        this.msg = baseError.getErrorMsg();
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
