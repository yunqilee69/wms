package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.DictError;
import com.yunqi.backend.mapper.DictTypeMapper;
import com.yunqi.backend.model.dto.DictTypeDTO;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.service.DictItemService;
import com.yunqi.backend.service.DictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    DictTypeMapper dictTypeMapper;

    @Resource
    DictItemService dictItemService;

    /**
     * 分页查询
     *
     * @param dictTypeDTO
     * @return
     */
    @Override
    public Page<DictType> getDictTypePage(DictTypeDTO dictTypeDTO) {
        Page<DictType> page = PageUtils.getPage();
        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(dictTypeDTO.getName() != null, DictType::getName, dictTypeDTO.getName());
        wrapper.like(dictTypeDTO.getCode() != null, DictType::getCode, dictTypeDTO.getCode());
        wrapper.eq(dictTypeDTO.getStatus() != null, DictType::getStatus, dictTypeDTO.getStatus());
        wrapper.orderByAsc(DictType::getOrderNum);
        return dictTypeMapper.selectPage(page, wrapper);
    }

    /**
     * 获取全部的字典
     * @return
     */
    @Override
    public List<DictType> getDictTypeAll() {
        return dictTypeMapper.selectList(null);
    }

    @Override
    public void saveDictType(DictTypeDTO dictTypeDTO) {
        DictType dictType = new DictType();
        BeanUtils.copyProperties(dictTypeDTO, dictType);

        save(dictType);
    }

    @Override
    public void updateDictType(DictTypeDTO dictTypeDTO) {
        if (dictTypeDTO.getId() == null || dictTypeDTO.getCode() == null) {
            throw new BizException(DictError.UPDATE_ERROR);
        }

        LambdaUpdateWrapper<DictType> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(dictTypeDTO.getName() != null, DictType::getName, dictTypeDTO.getName());
        wrapper.set(dictTypeDTO.getCode() != null, DictType::getCode, dictTypeDTO.getCode());
        wrapper.set(dictTypeDTO.getStatus() != null, DictType::getStatus, dictTypeDTO.getStatus());
        wrapper.set(dictTypeDTO.getOrderNum() != 0, DictType::getOrderNum, dictTypeDTO.getOrderNum());
        wrapper.set(dictTypeDTO.getRemark() != null, DictType::getRemark, dictTypeDTO.getRemark());
        wrapper.eq(dictTypeDTO.getId() != null, DictType::getId, dictTypeDTO.getId());
        update(wrapper);

        LambdaUpdateWrapper<DictItem> itemWrapper = new LambdaUpdateWrapper<>();
        itemWrapper.set(dictTypeDTO.getCode() != null, DictItem::getTypeCode, dictTypeDTO.getCode());
        itemWrapper.eq(DictItem::getTypeId, dictTypeDTO.getId());
        dictItemService.update(itemWrapper);
    }

    @Override
    public void deleteDictType(List<Long> ids) {
        removeByIds(ids);

        LambdaQueryWrapper<DictItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(DictItem::getTypeId, ids);
        dictItemService.remove(itemWrapper);
    }
}
