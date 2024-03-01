package com.yunqi.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 字典类型DTO
 * @author liyunqi
 */
@Data
public class DictTypeDTO {

    private Long id;

    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    private String name;

    /**
     * 字典编码
     */
    @NotNull(message = "字典名称不能为空")
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private int orderNum;

    /**
     * 状态
     */
    //@NotNull(message = "状态不能为空")
    private String status;
}
