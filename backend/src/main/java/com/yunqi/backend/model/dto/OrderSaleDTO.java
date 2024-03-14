package com.yunqi.backend.model.dto;

import com.yunqi.backend.common.base.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单DTO
 * @author liyunqi
 */
@Data
public class OrderSaleDTO extends BaseDTO {

    private Long id;

    /**
     * 单据号
     */
    private String documentCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 收件人
     */
    private String receiverName;

    /**
     * 收件人电话
     */
    private String receiverPhone;

    /**
     * 收货时间
     */
    private LocalDateTime receiptTime;

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
     * 退货金额
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
