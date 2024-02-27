package com.yunqi.backend.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 登录的请求体
 * @author liyunqi
 */
@Data
public class LoginRequest {
    /**
     * 账号
     */
    @NotNull(message = "账号不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "验证码不能为空")
    private String code;

    /**
     * 验证码对应的uuid
     */
    @NotNull(message = "系统验证码模块错误,请联系管理员")
    private String uuid;
}
