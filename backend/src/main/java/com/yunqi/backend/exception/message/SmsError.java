package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum SmsError implements BaseError {
    PHONE_NOT_VALIDATE(40001,"手机号不合法"),
    SEND_SMS_FAILED(40002, "发送短信失败");

    int code;
    String msg;

    SmsError(int code, String msg) {
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
