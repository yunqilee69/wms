package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * 供应商
 * @author liyunqi
 */
@Data
@TableName("tb_user_supplier")
public class Supplier extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 联系人名称
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
