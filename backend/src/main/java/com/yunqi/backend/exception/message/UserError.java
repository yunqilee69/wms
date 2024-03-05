package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum UserError implements BaseError {

    USERNAME_OR_PASSWORD_ERROR(10001, "用户名或密码错误,请重试"),
    USERNAME_OR_PASSWORD_EMPTY(10002, "用户名或密码不能为空"),
    CAPTCHA_ERROR(10003, "验证码错误"),
    PASSWORD_NOT_MATCH(10004, "两次输入的密码不一致"),
    PHONE_NUMBER_ERROR(10005,"请检查手机号后重试"),
    USERNAME_NOT_EXISTS(10006, "账号不存在"),
    USERNAME_ALREADY_EXISTS(10007, "该账号已存在"),
    USER_ROLE_ID_NULL(10008, "用户或角色不存在"),
    OLD_PASSWORD_ERROR(10009, "旧密码错误，请重试"),
    RESET_PASSWORD_ERROR(10010, "重置密码失败，请重试");

    int code;
    String msg;

    UserError(int code, String msg) {
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
