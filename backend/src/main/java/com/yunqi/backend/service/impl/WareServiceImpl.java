package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.RecordError;
import com.yunqi.backend.exception.message.WareError;
import com.yunqi.backend.mapper.WareMapper;
import com.yunqi.backend.mapper.WareMoneyMapper;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.model.entity.WareMoney;
import com.yunqi.backend.service.RecordService;
import com.yunqi.backend.service.WareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {
    @Resource
    WareMapper wareMapper;

    @Resource
    WareMoneyMapper wareMoneyMapper;

    @Resource
    RecordService recordService;

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
        if (StringUtils.isAnyEmpty(wareDTO.getName(), wareDTO.getBarCode())
            || wareDTO.getSalePrice() == null || wareDTO.getPurchasePrice() == null) {
            throw new BizException(WareError.PARAMS_EMPTY);
        }

        Ware ware = new Ware();
        BeanUtils.copyProperties(wareDTO, ware);

        LambdaQueryWrapper<Ware> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ware::getBarCode, ware.getBarCode());
        List<Ware> wareList = wareMapper.selectList(wrapper);
        if (wareList != null && wareList.size()> 0) {
            // 条形码重复
            throw new BizException(WareError.BAR_CODE_REPEAT);
        }

        wareMapper.insert(ware);

        // 新增货物需要保存货物价格的变化
        WareMoney wareMoney = new WareMoney();
        wareMoney.setWareId(ware.getId());
        wareMoney.setSalePrice(ware.getSalePrice());
        wareMoney.setPurchasePrice(ware.getPurchasePrice());
        wareMoney.setRecordTime(LocalDateTime.now());
        wareMoneyMapper.insert(wareMoney);
    }

    @Override
    public void updateWare(WareDTO wareDTO) {
        if (StringUtils.isAnyEmpty(wareDTO.getName(), wareDTO.getBarCode())
                || wareDTO.getSalePrice() == null || wareDTO.getPurchasePrice() == null) {
            throw new BizException(WareError.PARAMS_EMPTY);
        }

        Ware ware = new Ware();
        BeanUtils.copyProperties(wareDTO, ware);

        Ware tempWare = wareMapper.selectById(wareDTO.getId());
        if (tempWare.getPurchasePrice().compareTo(wareDTO.getPurchasePrice()) != 0
                || tempWare.getSalePrice().compareTo(wareDTO.getSalePrice()) != 0) {
            // 更新了进价和售价，需要更新库存，并保存货物价格的变化
            WareMoney wareMoney = new WareMoney();
            wareMoney.setWareId(ware.getId());
            wareMoney.setSalePrice(ware.getSalePrice());
            wareMoney.setPurchasePrice(ware.getPurchasePrice());
            wareMoney.setRecordTime(LocalDateTime.now());
            wareMoneyMapper.insert(wareMoney);

            // 更新库存
            List<Record> recordList = recordService.getRecordByWareId(ware.getId());
            for (Record record : recordList) {
                // 更新总价值
                record.setTotalAmount(ware.getPurchasePrice().multiply(BigDecimal.valueOf(record.getNumber())));
                recordService.updateById(record);
            }
        }

        if (tempWare.getQualityMonth() != ware.getQualityMonth()) {
            // 更新了保质期，更新库存
            List<Record> recordList = recordService.getRecordByWareId(ware.getId());
            for (Record record : recordList) {
                // 更新保质期，有效期，生产日期
                record.setQualityMonth(ware.getQualityMonth());
                LocalDate productionDate = record.getProductionDate();
                record.setGuaranteeDate(productionDate.plusMonths(ware.getQualityMonth()));
                recordService.updateById(record);
            }
        }

        wareMapper.updateById(ware);
    }

    @Override
    public void deleteWare(List<Long> wareIds) {
        for (Long wareId : wareIds) {
            List<Record> recordList = recordService.getRecordByWareId(wareId);
            if (recordList.size() != 0) {
                throw new BizException(WareError.WARE_IS_USING_IN_RECORD);
            }
        }
        removeBatchByIds(wareIds);
    }
}
