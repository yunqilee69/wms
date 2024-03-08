package com.yunqi.backend.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 单据号工具类
 * @author liyunqi
 */
public class DocumentUtils {

    /**
     * 生成单据号
     * @return
     */
    public static String generateDocumentNumber(String type) {
        // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddSS");
        String date = dateFormat.format(new Date());

        // 生成一个四位的随机数
        Random random = new Random();
        int randomNum = random.nextInt(1000);

        // 生成单据号
        return type + date + randomNum;
    }


}
