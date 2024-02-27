package com.yunqi.backend.model.entity;

import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * 字典表
 * @author liyunqi
 */
@Data
public class DictCode extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典编码
     */
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
    private int status;

}
