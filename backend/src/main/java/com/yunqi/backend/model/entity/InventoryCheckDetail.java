package com.yunqi.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

/**
 * 库存盘点细节表
 * @author liyunqi
 */
@Data
@TableName("tb_inventory_check_detail")
public class InventoryCheckDetail{

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
}
