package com.yunqi.backend.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import com.yunqi.backend.config.properties.CaptchaProperties;
import com.yunqi.backend.service.CaptchaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *  验证码服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    CaptchaProperties captchaProperties;

    // 图片长度
    private final static int HEIGHT = 60;

    // 图片宽度
    private final static int WEIGHT = 160;

    // 干扰线数量
    private final static int LINE_COUNT = 5;

    // 圆圈数量
    private final static int CIRCLE_COUNT = 5;

    // 扭曲干扰
    private final static int THICKNESS = 3;

    /**
     * 生成验证码
     * @return
     */
    @Override
    public AbstractCaptcha createCaptcha() {
        AbstractCaptcha result = null;
        String type = captchaProperties.getType();
        if (type.equals("Line")) {
            // 使用线段干扰的验证码
            result = CaptchaUtil.createLineCaptcha(WEIGHT, HEIGHT, captchaProperties.getCodeCount(), LINE_COUNT);
        } else if (type.equals("Circle")) {
            // 使用圆圈干扰验证码
            result = CaptchaUtil.createCircleCaptcha(WEIGHT, HEIGHT, captchaProperties.getCodeCount(), CIRCLE_COUNT);
        } else if (type.equals("Shear")) {
            // 扭曲干扰验证码
            result =  CaptchaUtil.createShearCaptcha(WEIGHT, HEIGHT, captchaProperties.getCodeCount(), THICKNESS);
        } else {
            // 默认使用线段干扰的验证码
            result = CaptchaUtil.createLineCaptcha(WEIGHT, HEIGHT, captchaProperties.getCodeCount(), LINE_COUNT);
        }
        return result;
    }

    @Override
    public int getExpireTime() {
        return captchaProperties.getExpireTime();
    }
}
