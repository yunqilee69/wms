package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.mapper.LocationMapper;
import com.yunqi.backend.model.dto.LocationDTO;
import com.yunqi.backend.model.entity.Location;
import com.yunqi.backend.service.LocationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {
    @Resource
    LocationMapper locationMapper;

    @Override
    public Page<Location> getLocationPage(LocationDTO locationDTO) {
        Page<Location> page = PageUtils.getPage();
        LambdaQueryWrapper<Location> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(locationDTO.getName() != null, Location::getName, locationDTO.getName());
        wrapper.eq(locationDTO.getStatus() != null, Location::getStatus, locationDTO.getStatus());
        return locationMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveLocation(LocationDTO locationDTO) {
        Location location = new Location();
        BeanUtils.copyProperties(locationDTO, location);
        locationMapper.insert(location);
    }

    @Override
    public void updateLocation(LocationDTO locationDTO) {
        Location location = new Location();
        BeanUtils.copyProperties(locationDTO, location);
        locationMapper.updateById(location);
    }
}
