package com.yunqi.backend.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author liyunqi
 */
@Configuration
@Data
public class CaptchaProperties {

    @Value("${captcha.type}")
    private String type;

    @Value("${captcha.expireTime}")
    private int expireTime;

    @Value("${captcha.codeCount}")
    private int codeCount;
}
