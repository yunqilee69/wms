package com.yunqi.backend.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseDTO;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

/**
 * 库存盘点细节DTO
 * @author liyunqi
 */
@Data
public class InventoryCheckDetailDTO{

    /**
     * 主键
     */
    private Long id;

    /**
     * 盘点id
     */
    private Long checkId;

    /**
     * 库存记录id
     */
    private Long recordId;

    /**
     * 货位名称
     */
    private String locationName;

    /**
     * 盘点前数量
     */
    private int preNumber;

    /**
     * 盘点后数量
     */
    private int nowNumber;

    /**
     * 盘点前生产日期
     */
    private LocalDate preProductionDate;

    /**
     * 盘点后生产日期
     */
    private LocalDate nowProductionDate;

    /**
     * 货物名称
     */
    private String wareName;

    /**
     * 货物品牌
     */
    private String wareBrand;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 货物图片
     */
    private String picture;
}
