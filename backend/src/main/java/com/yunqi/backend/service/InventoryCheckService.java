package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.InventoryCheckDTO;
import com.yunqi.backend.model.dto.InventoryCheckDTO;
import com.yunqi.backend.model.entity.InventoryCheck;

import java.util.List;

/**
 * 盘点服务类
 * @author liyunqi
 */
public interface InventoryCheckService extends IService<InventoryCheck> {
    /**
     * 分页查询
     * @param inventoryCheckDTO
     * @return
     */
    Page<InventoryCheck> getInventoryCheckPage(InventoryCheckDTO inventoryCheckDTO);

    /**
     * 新增
     * @param inventoryCheckDTO
     */
    void saveInventoryCheck(InventoryCheckDTO inventoryCheckDTO);

    /**
     * 更新
     * @param inventoryCheckDTO
     */
    void updateInventoryCheck(InventoryCheckDTO inventoryCheckDTO);

    /**
     * 删除
     * @param ids
     */
    void deleteInventoryCheck(List<Long> ids);

    /**
     * 应用盘点单，将数据库的数据进行修改
     * @param checkId
     */
    void apply(Long checkId);
}
