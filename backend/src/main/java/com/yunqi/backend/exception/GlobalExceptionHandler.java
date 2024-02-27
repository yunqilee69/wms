package com.yunqi.backend.exception;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liyunqi
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理@Validated注解检查出的异常
     * @param e
     * @return
     */
    @ExceptionHandler({BindException.class})
    public Result<String> handleBindException(BindException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        log.error("Request请求体校验失败：{}", objectError.getDefaultMessage());
        return Result.fail(objectError.getDefaultMessage());
    }

    /**
     * 处理业务异常
     * @param e
     * @return
     */
    @ExceptionHandler({BizException.class})
    public Result<String> handleBizException(BizException e) {
        log.error("用户id：{}，出现了业务异常：{}", SecurityUtils.getLoginUser().getUserId(),e.getMsg());
        return Result.fail(e.getCode(), e.getMsg());
    }
}
