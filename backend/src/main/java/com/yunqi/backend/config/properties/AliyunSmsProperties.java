package com.yunqi.backend.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author liyunqi
 */
@Component
@ConfigurationProperties("aliyun.sms")
@Data
public class AliyunSmsProperties {

    private String keyId;

    private String keySecret;

    private List<SmsTemplate> templateList;

    /**
     * sms模板内部类
     */
    @Data
    public static class SmsTemplate {
        private String name;
        private String templateCode;
        private String signName;
    }
}
