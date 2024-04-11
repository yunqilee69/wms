package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.LocationDTO;
import com.yunqi.backend.model.entity.Location;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.LocationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @PreAuthorize("@sps.hasPermi('inventory:waresLocation:list')")
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
    @PreAuthorize("@sps.hasPermi('inventory:waresLocation:query')")
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
    @PreAuthorize("@sps.hasPermi('inventory:waresLocation:add')")
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
    @PreAuthorize("@sps.hasPermi('inventory:waresLocation:delete')")
    @DeleteMapping("/{locationIds}")
    public Result delete(@PathVariable List<Long> locationIds) {
        if (locationIds == null || locationIds.size() == 0) {
            return Result.fail("货位id不能为空");
        }
        locationService.deleteLocation(locationIds);
        return Result.success();
    }

    /**
     * 更新
     * @param locationDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresLocation:edit')")
    @PutMapping
    public Result update(@RequestBody LocationDTO locationDTO) {
        locationService.updateLocation(locationDTO);
        return Result.success();
    }

    /**
     * 获取数据库所有的货物数据，并组装为前端select组件的数据
     * @return
     */
    @GetMapping("/getSelect")
    public Result getSelect() {
        LambdaQueryWrapper<Location> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Location::getStatus, "0");
        return Result.success(locationService.getBaseMapper().selectList(wrapper));
    }
}
