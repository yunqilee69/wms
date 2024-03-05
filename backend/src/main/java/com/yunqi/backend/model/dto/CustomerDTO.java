package com.yunqi.backend.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * 客户DTO
 * @author liyunqi
 */
@Data
public class CustomerDTO extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 地址
     */
    private String address;
}
