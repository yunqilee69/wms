package com.yunqi.backend.core.security.handle;

import com.alibaba.fastjson2.JSON;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.ServletUtils;
import com.yunqi.backend.exception.message.SystemError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = SystemError.PERMS_DENIED.getCode();
        String msg = SystemError.PERMS_DENIED.getErrorMsg();
        ServletUtils.renderString(response, JSON.toJSONString(Result.fail(code, msg)));
    }
}
