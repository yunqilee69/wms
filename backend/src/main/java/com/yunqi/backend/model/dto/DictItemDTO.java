package com.yunqi.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 字典项DTO
 * @author liyunqi
 */
@Data
public class DictItemDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 字典编码
     */
    @NotNull(message = "字典编码不能为空")
    private String typeCode;

    /**
     * 标签（显示的名称）
     */
    @NotNull(message = "字典标签不能为空")
    private String label;

    /**
     * 字典项值
     */
    @NotNull(message = "字典键值不能为空")
    private String value;

    /**
     * 排序
     */
    @NotNull(message = "字典排序不能为空")
    private int orderNum;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表格回显格式
     */
    private String listClass;
}
