package com.yunqi.backend.service;

import java.io.InputStream;

/**
 * 短信服务
 * @author liyunqi
 */
public interface AliyunService {

    /**
     * 发送短信
     * @param phone 要发送的手机号
     * @param code 键值对，例如验证码：code：1234
     * @return
     */
    void sendCaptchaCode(String phone, String code);

    /**
     *  发送重置密码短信
     * @param phone
     * @param username
     * @param pwd
     * @return
     */
    void sendResetPwd(String phone, String username, String pwd);

    /**
     * 上传文件，返回访问路径
     * @param inputStream
     * @param fileName
     * @return
     */
    String ossUpload(InputStream inputStream, String fileName);

    /**
     * 删除文件，成功为true
     * @param fileName
     * @return
     */
    boolean ossDelete(String fileName);
}
