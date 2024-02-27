package com.yunqi.backend.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liyunqi
 */
@Data
public class RegisterRequest {

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
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    private String phone;
}
