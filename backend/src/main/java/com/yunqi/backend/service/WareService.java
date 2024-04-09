package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.model.entity.Ware;

import java.util.List;

/**
 * @author liyunqi
 */
public interface WareService extends IService<Ware> {
    /**
     * 分页查询
     * @param wareDTO
     * @return
     */
    Page<Ware> getWarePage(WareDTO wareDTO);

    /**
     * 新增
     * @param wareDTO
     */
    void saveWare(WareDTO wareDTO);

    /**
     * 更新
     * @param wareDTO
     */
    void updateWare(WareDTO wareDTO);

    /**
     * 删除货物
     * @param wareIds
     */
    void deleteWare(List<Long> wareIds);
}
