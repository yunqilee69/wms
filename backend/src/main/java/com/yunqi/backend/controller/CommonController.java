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
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.SystemError;
import com.yunqi.backend.service.AliyunService;
import com.yunqi.backend.service.CaptchaService;
import org.springframework.data.annotation.Id;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统公共控制
 *
 * @author ruoyi
 */
@RestController
@RequestMapping
public class CommonController {

    @Resource
    RedisCache redisCache;

    @Resource
    CaptchaService captchaService;

    @Resource
    AliyunService aliyunService;

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

    private String convertImageToBase64(BufferedImage image) {
        String base64Image = null;
        try {
            FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            if (!ImageIO.write(image, "PNG", os)) {
                throw new BizException(SystemError.CAPTCHA_GENERATE_ERROR);
            }
            byte[] imageBytes = os.toByteArray();

            base64Image = Base64.getEncoder().encodeToString(imageBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    /**
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = IdUtil.simpleUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = aliyunService.ossUpload(file.getInputStream(), filename);
        return Result.success(url);
    }
}
