package com.yunqi.backend.model.entity;

import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * 字典项表
 * @author liyunqi
 */
@Data
public class DictItem extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 字典编码
     */
    private Long dictCode;

    /**
     * 标签（显示的名称）
     */
    private String label;

    /**
     * 字典项值
     */
    private String value;

    /**
     * 排序
     */
    private int orderNum;

    /**
     * 状态
     */
    private int status;

}
