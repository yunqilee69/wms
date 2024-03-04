package com.yunqi.backend.model.entity;

import lombok.Data;

/**
 * 用户角色关联表
 * @author liyunqi
 */
@Data
public class UserRole {

    private Long id;

    private Long userId;

    private Long roleId;

}
