package com.yunqi.backend.service;

import cn.hutool.captcha.AbstractCaptcha;

/**
 * 验证码生成
 * @author liyunqi
 */
public interface CaptchaService {

    /**
     * 生成验证码
     * @return
     */
    AbstractCaptcha createCaptcha();

    /**
     * 获取过期时间
     * @return
     */
    int getExpireTime();

}
