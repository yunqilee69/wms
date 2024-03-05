package com.yunqi.backend.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.config.properties.AliyunSmsProperties;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.SmsError;
import com.yunqi.backend.service.AliyunSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liyunqi
 */
@Service
@Slf4j
public class AliyunSmsServiceImpl implements AliyunSmsService {

    @Resource
    AliyunSmsProperties smsProperties;

    @Resource
    RedisCache redisCache;

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    private Client createClient() throws Exception {
        Config config = new Config();
        config.setAccessKeyId(smsProperties.getKeyId());
        config.setAccessKeySecret(smsProperties.getKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        // 发送短信从上海发出
        config.endpoint = "dysmsapi.aliyuncs.com";
        config.setRegionId("cn-shanghai");
        return new Client(config);
    }

    /**
     * 统一进行发送，并进行异常捕获
     * @param sendSmsRequest
     */
    private boolean sendSms(SendSmsRequest sendSmsRequest) {
        try {
            Client client = createClient();
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            log.info("短信发送结果：{}", new Gson().toJson(response.body));
            return true;
        } catch (TeaException error) {
            // 错误 message
            log.error(error.getMessage());
            // 诊断地址
            log.error(error.getData().get("Recommend").toString());
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception error) {
            TeaException taxError = new TeaException(error.getMessage(), error);
            // 错误 message
            log.error(taxError.getMessage());
            // 诊断地址
            log.error(taxError.getData().get("Recommend").toString());
            com.aliyun.teautil.Common.assertAsString(taxError.message);
        }
        return false;
    }

    /**
     * 校验手机号是否合法
     * @param phone
     * @return
     */
    private boolean validatePhone(String phone) {
        String regex = "^1[3-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    public void sendCaptchaCode(String phone, String code) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        List<AliyunSmsProperties.SmsTemplate> templateList = smsProperties.getTemplateList();
        for (AliyunSmsProperties.SmsTemplate smsTemplate : templateList) {
            if (smsTemplate.getName().equals("CAPTCHA_CODE")) {
                sendSmsRequest.setSignName(smsTemplate.getSignName());
                sendSmsRequest.setTemplateCode(smsTemplate.getTemplateCode());
                break;
            }
        }

        if (!validatePhone(phone)) {
            throw new BizException(SmsError.PHONE_NOT_VALIDATE);
        }

        sendSmsRequest.setPhoneNumbers(phone);

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        sendSmsRequest.setTemplateParam(new Gson().toJson(params));

        if (!sendSms(sendSmsRequest)) {
            throw new BizException(SmsError.SEND_SMS_FAILED);
        }
        log.info("验证码发送成功，phone:{}， code:{}", phone, code);

        // 将验证码缓存到redis中
        // 有效时间固定15分钟，在短信中已说明
        redisCache.setCacheObject(CacheConstants.SMS_CODE_KEY+phone, code, 15, TimeUnit.MINUTES);
    }

    @Override
    public void sendResetPwd(String phone, String nickname, String pwd) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        List<AliyunSmsProperties.SmsTemplate> templateList = smsProperties.getTemplateList();
        for (AliyunSmsProperties.SmsTemplate smsTemplate : templateList) {
            if (smsTemplate.getName().equals("RESET_PWT")) {
                sendSmsRequest.setSignName(smsTemplate.getSignName());
                sendSmsRequest.setTemplateCode(smsTemplate.getTemplateCode());
                break;
            }
        }

        if (!validatePhone(phone)) {
            throw new BizException(SmsError.PHONE_NOT_VALIDATE);
        }

        sendSmsRequest.setPhoneNumbers(phone);

        Map<String, Object> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("pwd", pwd);
        sendSmsRequest.setTemplateParam(new Gson().toJson(params));

        if (!sendSms(sendSmsRequest)) {
            throw new BizException(SmsError.SEND_SMS_FAILED);
        }
        log.info("重置密码发送成功，phone:{}， nickname:{}", phone, nickname);
    }
}
