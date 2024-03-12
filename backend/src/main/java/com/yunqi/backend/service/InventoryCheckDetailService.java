package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.InventoryCheckDetailDTO;
import com.yunqi.backend.model.entity.InventoryCheckDetail;

import java.util.List;

/**
 * 盘点服务类
 * @author liyunqi
 */
public interface InventoryCheckDetailService extends IService<InventoryCheckDetail> {
    /**
     * 分页查询
     * @param inventoryCheckDTO
     * @return
     */
    Page<InventoryCheckDetailDTO> getInventoryCheckDetailPage(InventoryCheckDetailDTO inventoryCheckDTO);

    /**
     * 新增
     * @param inventoryCheckDTO
     */
    void saveInventoryCheckDetail(InventoryCheckDetailDTO inventoryCheckDTO);

    /**
     * 更新
     * @param inventoryCheckDTO
     */
    void updateInventoryCheckDetail(InventoryCheckDetailDTO inventoryCheckDTO);

    /**
     * 根据checkDetailId获取DTO
     * @param checkDetailId
     * @return
     */
    InventoryCheckDetailDTO selectById(Long checkDetailId);

    /**
     * 添加待盘点的库存记录
     * @param checkId
     * @param recordIds
     */
    void addCheckRecordAll(Long checkId, Long[] recordIds);
}
