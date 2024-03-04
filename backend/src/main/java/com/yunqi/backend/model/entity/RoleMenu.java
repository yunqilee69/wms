package com.yunqi.backend.model.entity;

import lombok.Data;

/**
 * 角色菜单关联表
 * @author liyunqi
 */
@Data
public class RoleMenu {

    private Long id;

    private Long roleId;

    private Long menuId;

}
