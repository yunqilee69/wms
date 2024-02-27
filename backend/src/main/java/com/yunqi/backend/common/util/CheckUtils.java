package com.yunqi.backend.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常见的校验,手机号
 * @author liyunqi
 */
public class CheckUtils {

    /**
     * 校验手机号
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        String regex = "^?1[3-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
