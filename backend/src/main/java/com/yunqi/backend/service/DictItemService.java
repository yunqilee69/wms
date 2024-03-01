package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.DictItemDTO;
import com.yunqi.backend.model.entity.DictItem;

import java.util.List;

/**
 * @author liyunqi
 */
public interface DictItemService extends IService<DictItem> {
    /**
     * 分页查询
     * @param
     */
    Page<DictItem> getDictItemPage(DictItemDTO dictItemDTO);

    /**
     * 根据code获取字段项
     */
    List<DictItem> getDictItemByCode(String code);

    /**
     * 保存字典项
     * @param dictItemDTO
     */
    void saveDictItem(DictItemDTO dictItemDTO);

    /**
     * 更新DictItem
     * @param dictItemDTO
     */
    void updateDictItem(DictItemDTO dictItemDTO);

    /**
     * 根据itemId进行删除
     * @param dictItemIds
     */
    void deleteDictItem(List<Long> dictItemIds);
}
