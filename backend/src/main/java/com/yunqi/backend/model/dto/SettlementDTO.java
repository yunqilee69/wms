package com.yunqi.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单结算DTO
 * @author liyunqi
 */
@Data
public class SettlementDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 支付截图
     */
    private String[] pictureList;

    /**
     * 备注
     */
    private String remark;
}
