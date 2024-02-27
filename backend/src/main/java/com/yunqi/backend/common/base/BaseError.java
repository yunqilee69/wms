package com.yunqi.backend.common.base;

/**
 * @author liyunqi
 */
public interface BaseError {

    /**
     * 获取错误码
     * @return
     */
    int getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getErrorMsg();
}
