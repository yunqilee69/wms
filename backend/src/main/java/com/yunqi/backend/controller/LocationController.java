package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.LocationDTO;
import com.yunqi.backend.model.entity.Location;
import com.yunqi.backend.service.LocationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 货位信息控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/inventory/location")
public class LocationController {

    @Resource
    LocationService locationService;

    /**
     * 分页查询
     * @param locationDTO
     * @return
     */
    @GetMapping("/list")
    public Result getList(LocationDTO locationDTO) {
        Page<Location> page = locationService.getLocationPage(locationDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param locationId
     * @return
     */
    @GetMapping("/{locationId}")
    public Result getOne(@PathVariable Long locationId) {
        Location location = locationService.getById(locationId);
        return Result.success(location);
    }

    /**
     * 新增
     * @param locationDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody LocationDTO locationDTO) {
        locationService.saveLocation(locationDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param locationIds
     * @return
     */
    @DeleteMapping("/{locationIds}")
    public Result delete(@PathVariable List<Long> locationIds) {
        if (locationIds == null || locationIds.size() == 0) {
            return Result.fail("货位id不能为空");
        }
        locationService.removeBatchByIds(locationIds);
        return Result.success();
    }

    /**
     * 更新
     * @param locationDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody LocationDTO locationDTO) {
        locationService.updateLocation(locationDTO);
        return Result.success();
    }

}
