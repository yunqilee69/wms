package com.yunqi.backend.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.util.IdUtil;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.service.CaptchaService;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping
public class CaptchaController {

    @Resource
    RedisCache redisCache;

    @Resource
    CaptchaService captchaService;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public Result getCode(HttpServletResponse response) throws IOException {
        AbstractCaptcha captcha = captchaService.createCaptcha();

        // 设置redis缓存
        String code = captcha.getCode();
        String uuid = IdUtil.simpleUUID();
        redisCache.setCacheObject(CacheConstants.CAPTCHA_CODE_KEY+ uuid, code, captchaService.getExpireTime(), TimeUnit.MINUTES);

        // 生成图像
        BufferedImage image = captcha.getImage();
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", convertImageToBase64(image));
        return Result.success(map);
    }

    public String convertImageToBase64(BufferedImage image) {
        String base64Image = null;
        try {
            FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            byte[] imageBytes = os.toByteArray();

            base64Image = Base64.getEncoder().encodeToString(imageBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping("/captchaImage")
//    public Result getCode(HttpServletResponse response) throws IOException {
//        // 保存验证码信息
//        String uuid = IdUtil.simpleUUID();
//        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
//
//        String capStr = null, code = null;
//        BufferedImage image = null;
//
//        // 生成验证码
//        String captchaType = RuoYiConfig.getCaptchaType();
//        if ("math".equals(captchaType)) {
//            String capText = captchaProducerMath.createText();
//            capStr = capText.substring(0, capText.lastIndexOf("@"));
//            code = capText.substring(capText.lastIndexOf("@") + 1);
//            image = captchaProducerMath.createImage(capStr);
//        } else if ("char".equals(captchaType)) {
//            capStr = code = captchaProducer.createText();
//            image = captchaProducer.createImage(capStr);
//        }
//
//        redisCache.setCacheObject(verifyKey, code, SystemConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
//        // 转换流信息写出
//        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
//        try {
//            ImageIO.write(image, "jpg", os);
//        } catch (IOException e) {
//            return AjaxResult.error(e.getMessage());
//        }
//
//        ajax.put("uuid", uuid);
//        ajax.put("img", Base64.encode(os.toByteArray()));
//        return ajax;
//    }
}
