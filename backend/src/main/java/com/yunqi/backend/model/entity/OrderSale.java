package com.yunqi.backend.model.entity;

import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单
 * @author liyunqi
 */
@Data
public class OrderSale extends BaseEntity {

    private Long id;

    /**
     * 单据号
     */
    private String documentCode;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 送货人名称
     */
    private String deliveryName;

    /**
     * 送货人电话
     */
    private String deliveryPhone;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户地址
     */
    private String customerAddress;

    /**
     * 送货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 货物数量
     */
    private int originNumber;

    /**
     * 原货物金额
     */
    private BigDecimal originAmount;

    /**
     * 退货数量
     */
    private int returnNumber;

    /**
     * 退货总金额
     */
    private BigDecimal returnAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 实际付款金额
     */
    private BigDecimal actualAmount;
}
