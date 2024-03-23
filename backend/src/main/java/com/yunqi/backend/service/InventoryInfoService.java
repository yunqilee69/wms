package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.entity.InventoryInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liyunqi
 */
public interface InventoryInfoService extends IService<InventoryInfo> {

    /**
     * 根据日期获取该天的仓库货物总数量
     * @param begin
     * @param end
     * @return
     */
    int getNumberByDate(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据日期获取该天的仓库货物总金额
     * @param begin
     * @param end
     * @re
     */
    BigDecimal getAmountByDate(LocalDateTime begin, LocalDateTime end);

}
