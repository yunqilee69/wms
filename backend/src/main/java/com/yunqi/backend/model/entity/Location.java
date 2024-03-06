package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_location")
public class Location extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 货位名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 容量
     */
    private String capacity;

    /**
     * 备注
     */
    private String remark;

}
