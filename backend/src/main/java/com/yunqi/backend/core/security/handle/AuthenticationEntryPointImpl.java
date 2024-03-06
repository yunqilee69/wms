package com.yunqi.backend.core.security.handle;

import com.google.gson.Gson;
import com.yunqi.backend.common.result.Result;
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

        // 设置返回数据
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(new Gson().toJson(Result.fail(code, msg)));

    }
}
