package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.mapper.WareMapper;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.WareService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {
    @Resource
    WareMapper wareMapper;

    @Override
    public Page<Ware> getWarePage(WareDTO wareDTO) {
        Page<Ware> page = PageUtils.getPage();
        LambdaQueryWrapper<Ware> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(wareDTO.getName() != null, Ware::getName, wareDTO.getName());
        wrapper.eq(wareDTO.getBrand() != null, Ware::getBrand, wareDTO.getBrand());
        wrapper.eq(wareDTO.getCategory() != null, Ware::getCategory, wareDTO.getCategory());
        wrapper.like(wareDTO.getBarCode() != null, Ware::getBarCode, wareDTO.getBarCode());
        return wareMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveWare(WareDTO wareDTO) {
        Ware ware = new Ware();
        BeanUtils.copyProperties(wareDTO, ware);
        // TODO 货物 新增校验
        wareMapper.insert(ware);
    }

    @Override
    public void updateWare(WareDTO wareDTO) {
        Ware ware = new Ware();
        BeanUtils.copyProperties(wareDTO, ware);
        // TODO 货物 更新校验
        wareMapper.updateById(ware);
    }
}
