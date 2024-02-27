package com.yunqi.backend.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @author liyun
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    // 默认信息
    private final static int DEFAULT_SUCCESS_CODE = ResultCode.SUCCESS;
    private final static int DEFAULT_FAIL_CODE = ResultCode.ERROR;
    private final static String DEFAULT_SUCCESS_MSG = "操作成功";
    private final static String DEFAULT_FAIL_MSG = "系统错误，请联系管理员";

    private int code; //编码：0失败，1成功
    private String msg; //错误信息
    private T data; //数据

    /**
     *  操作成功，默认信息为“操作成功”，无返回结果
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        Result<T> result =  new Result<T>();
        result.code = DEFAULT_SUCCESS_CODE;
        result.msg = DEFAULT_SUCCESS_MSG;
        return result;
    }

    /**
     * 操作成功，默认信息为“操作成功”，自定义返回结果
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.code = DEFAULT_SUCCESS_CODE;
        result.msg = DEFAULT_SUCCESS_MSG;
        result.data = data;
        return result;
    }

    /**
     * 操作成功，自定义默认信息，无返回结果
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<T>();
        result.code = DEFAULT_SUCCESS_CODE;
        result.msg = msg;
        result.data = data;
        return result;
    }

    /**
     * 默认错误信息
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail() {
        Result<T> result = new Result<T>();
        result.code = DEFAULT_FAIL_CODE;
        result.msg = DEFAULT_FAIL_MSG;
        return result;
    }

    /**
     * 自定义错误信息
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<T>();
        result.code = DEFAULT_FAIL_CODE;
        result.msg = msg;
        return result;
    }

    /**
     * 自定义错误码及错误信息
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<T>();
        result.code = code;
        result.msg = msg;
        return result;
    }

}
