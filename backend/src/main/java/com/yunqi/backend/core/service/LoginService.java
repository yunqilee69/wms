package com.yunqi.backend.core.service;

import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.core.security.context.AuthenticationContextHolder;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.UserError;
import com.yunqi.backend.model.dto.LoginUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录
 * @author liyunqi
 */
@Service
public class LoginService {

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisCache redisCache;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录成功返回一个token
     *
     * @param username 账号
     * @param password 密码
     * @param code     验证码结果
     * @param uuid     验证码对应的uuid,用户输入的code与系统生成的进行对比
     * @return
     */
    public String login(String username, String password, String code, String uuid) {
        // 校验验证码
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);

        if (StringUtils.isEmpty(captcha) || !code.equalsIgnoreCase(captcha)) {
            throw new BizException(UserError.CAPTCHA_ERROR);
        }

        // 前置校验
        if (username.isEmpty() || password.isEmpty()) {
            throw new BizException(UserError.USERNAME_OR_PASSWORD_EMPTY);
        }

        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(UserError.USERNAME_OR_PASSWORD_ERROR);
        }

        LoginUserDTO loginUser = (LoginUserDTO) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);

    }
}
