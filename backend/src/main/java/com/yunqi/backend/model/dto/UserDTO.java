package com.yunqi.backend.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liyunqi
 */
@Data
public class UserDTO {
    /**
     * 主键
     */
    private Long id;

    /*
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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
     * 状态
     */
    private String status;

    /**
     * 用户所拥有的角色集合
     */
    List<Long> roleIds;
}
