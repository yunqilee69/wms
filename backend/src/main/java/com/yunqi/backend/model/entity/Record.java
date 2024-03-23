package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存记录
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_record")
public class Record extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 货位id
     */
    private Long locationId;

    /**
     * 货物id
     */
    private Long wareId;

    /**
     * 货位名称
     */
    private String locationName;

    /**
     * 数量
     */
    private int number;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 保质期（月）
     */
    private int qualityMonth;

    /**
     * 有效期
     */
    private LocalDate guaranteeDate;

    /**
     * 报警阈值
     */
    private int alarmThreshold;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

}
