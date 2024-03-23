package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记录货物的每一次价格变动
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_ware_money")
public class WareMoney {

    // 主键
    private Long id;

    // 货物id
    private Long wareId;

    // 售价
    private BigDecimal salePrice;

    // 进价
    private BigDecimal purchasePrice;

    // 记录时间
    private LocalDateTime recordTime;
}
