package com.yunqi.backend.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseDTO;
import com.yunqi.backend.model.entity.Ware;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存记录DTO
 * @author liyunqi
 */
@Data
public class RecordDTO extends BaseDTO {

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
    private int totalAmount;

    /**
     * 货物名称
     */
    private String wareName;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 货物品牌
     */
    private String wareBrand;

    /**
     * 货物规格
     */
    private String wareSpec;

    /**
     * 货物单位
     */
    private String wareUnit;

    /**
     * 货物图片
     */
    private String picture;
}
