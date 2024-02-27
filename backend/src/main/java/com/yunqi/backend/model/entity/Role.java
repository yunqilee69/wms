package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 角色状态（0正常 1停用）
     */
    private Integer status;

}
