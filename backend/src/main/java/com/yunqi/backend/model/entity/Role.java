package com.yunqi.backend.model.entity;

import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;


/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Data
public class Role extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

}
