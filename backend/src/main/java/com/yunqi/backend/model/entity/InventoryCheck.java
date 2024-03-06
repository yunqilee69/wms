package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * 库存盘点
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_check")
public class InventoryCheck extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 盘点单号
     */
    private String documentCode;

    /**
     * 盘点名称
     */
    private String name;

    /**
     * 盘点类型
     */
    private String type;

    /**
     * 盘点状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
