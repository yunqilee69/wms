package com.yunqi.backend.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yunqi.backend.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 完成公共字段的设置
 */
@Slf4j
@Component
public class AutoFillHandler implements MetaObjectHandler {

    /**
     * 插入时执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String nickname =  SecurityUtils.getLoginUser().getUser().getNickname();
        log.info("start insert fill ....");
        this.setFieldValByName("creator", nickname, metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updater", nickname, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 更新时执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String nickname =  SecurityUtils.getLoginUser().getUser().getNickname();
        log.info("start update fill ....");
        this.setFieldValByName("updater", nickname, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}

