package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.LocationDTO;
import com.yunqi.backend.model.entity.Location;

import java.util.List;

/**
 * @author liyunqi
 */
public interface LocationService extends IService<Location> {
    /**
     * 分页查询
     * @param locationDTO
     * @return
     */
    Page<Location> getLocationPage(LocationDTO locationDTO);

    /**
     * 新增
     * @param locationDTO
     */
    void saveLocation(LocationDTO locationDTO);

    /**
     * 更新
     * @param locationDTO
     */
    void updateLocation(LocationDTO locationDTO);

    /**
     * 删除货位
     * @param locationIds
     */
    void deleteLocation(List<Long> locationIds);
}
