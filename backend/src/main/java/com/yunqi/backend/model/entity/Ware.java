package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货物
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_ware")
public class Ware extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 货物名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 规格
     */
    private String spec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 进价
     */
    private BigDecimal purchasePrice;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 分类
     */
    private String category;

    /**
     * 图片
     */
    private String picture;

    /**
     * 保质期（月）
     */
    private int qualityMonth;
}
