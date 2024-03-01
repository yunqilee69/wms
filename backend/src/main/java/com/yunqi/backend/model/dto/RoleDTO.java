package com.yunqi.backend.model.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 角色DTO
 * @author liyunqi
 */
@Data
public class RoleDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    private String name;

    /**
     * 角色权限字符串
     */
    @NotNull(message = "权限字符不能为空")
    private String roleKey;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotNull(message = "角色状态不能为空")
    private String status;

}
