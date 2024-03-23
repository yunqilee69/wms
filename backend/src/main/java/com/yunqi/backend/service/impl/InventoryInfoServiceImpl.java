package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.mapper.InventoryInfoMapper;
import com.yunqi.backend.model.entity.InventoryInfo;
import com.yunqi.backend.service.InventoryInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class InventoryInfoServiceImpl extends ServiceImpl<InventoryInfoMapper, InventoryInfo> implements InventoryInfoService {

    @Resource
    InventoryInfoMapper inventoryInfoMapper;

    @Override
    public int getNumberByDate(LocalDateTime begin, LocalDateTime end) {
        LambdaQueryWrapper<InventoryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(InventoryInfo::getRecordTime, begin);
        wrapper.le(InventoryInfo::getRecordTime, end);
        InventoryInfo inventoryInfo = inventoryInfoMapper.selectOne(wrapper);
        if (inventoryInfo == null) {
            return 0;
        }
        return inventoryInfo.getTotalNumber();
    }

    @Override
    public BigDecimal getAmountByDate(LocalDateTime begin, LocalDateTime end) {
        LambdaQueryWrapper<InventoryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(InventoryInfo::getRecordTime, begin);
        wrapper.le(InventoryInfo::getRecordTime, end);
        InventoryInfo inventoryInfo = inventoryInfoMapper.selectOne(wrapper);
        if (inventoryInfo == null) {
            return BigDecimal.ZERO;
        }
        return inventoryInfo.getTotalAmount();
    }
}
