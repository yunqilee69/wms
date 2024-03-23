package com.yunqi.backend.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liyunqi
 */
@Data
public class InventoryInfo {

    // 主键
    private Long id;

    // 总价值
    private BigDecimal totalAmount;

    // 货物总数量
    private Integer totalNumber;

    // 记录时间
    private LocalDateTime recordTime;
}
