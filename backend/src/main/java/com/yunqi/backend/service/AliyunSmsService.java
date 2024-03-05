package com.yunqi.backend.service;

import java.util.List;
import java.util.Map;

/**
 * 短信服务
 * @author liyunqi
 */
public interface AliyunSmsService {

    /**
     * 发送短信
     * @param phone 要发送的手机号
     * @param code 键值对，例如验证码：code：1234
     * @return
     */
    void sendCaptchaCode(String phone, String code);

    /**
     *  发送重置密码短信
     * @param phone
     * @param username
     * @param pwd
     * @return
     */
    void sendResetPwd(String phone, String username, String pwd);
}
