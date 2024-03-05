package com.yunqi.backend.model.dto;

import lombok.Data;

/**
 * 角色下拉选项
 * @author liyunqi
 */
@Data
public class RoleOptionDTO {

    // 角色id
    private String roleId;

    // 角色名称
    private String roleName;

    // 角色权限字符
    private String roleKey;
}
