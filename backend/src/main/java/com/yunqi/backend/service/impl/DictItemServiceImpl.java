package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.DictError;
import com.yunqi.backend.mapper.DictItemMapper;
import com.yunqi.backend.model.dto.DictItemDTO;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.service.DictItemService;
import org.apache.commons.lang3.StringUtils;
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
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

    @Resource
    DictItemMapper dictItemMapper;

    @Override
    public Page<DictItem> getDictItemPage(DictItemDTO dictItemDTO) {
        Page<DictItem> page = PageUtils.getPage();
        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dictItemDTO.getTypeCode() != null, DictItem::getTypeCode, dictItemDTO.getTypeCode());
        wrapper.like(dictItemDTO.getLabel() != null, DictItem::getLabel, dictItemDTO.getLabel());
        wrapper.eq(dictItemDTO.getStatus() != null ,DictItem::getStatus, dictItemDTO.getStatus());
        wrapper.orderByAsc(DictItem::getOrderNum);
        return dictItemMapper.selectPage(page, wrapper);
    }

    @Override
    public List<DictItem> getDictItemByCode(String code) {
        LambdaQueryWrapper<DictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(code), DictItem::getTypeCode, code);
        return dictItemMapper.selectList(queryWrapper);
    }

    @Override
    public void saveDictItem(DictItemDTO dictItemDTO) {
        String typeCode = dictItemDTO.getTypeCode();
        String label = dictItemDTO.getLabel();
        String value = dictItemDTO.getValue();
        int orderNum = dictItemDTO.getOrderNum();
        String status = dictItemDTO.getStatus();
        String listClass = dictItemDTO.getListClass();
        if (StringUtils.isAnyEmpty(typeCode, label, value, status, listClass) || orderNum <= 0) {
            throw new BizException(DictError.PARAM_IS_EMPTY);
        }

        DictItem dictItem = new DictItem();
        BeanUtils.copyProperties(dictItemDTO, dictItem);
        save(dictItem);
    }

    @Override
    public void updateDictItem(DictItemDTO dictItemDTO) {

        LambdaUpdateWrapper<DictItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(dictItemDTO.getLabel() != null, DictItem::getLabel, dictItemDTO.getLabel());
        wrapper.set(dictItemDTO.getValue() != null, DictItem::getValue, dictItemDTO.getValue());
        wrapper.set(dictItemDTO.getOrderNum() != 0, DictItem::getOrderNum, dictItemDTO.getOrderNum());
        wrapper.set(dictItemDTO.getListClass() != null, DictItem::getListClass, dictItemDTO.getListClass());
        wrapper.set(dictItemDTO.getStatus() != null, DictItem::getStatus, dictItemDTO.getStatus());
        wrapper.set(dictItemDTO.getRemark() != null, DictItem::getRemark, dictItemDTO.getRemark());
        wrapper.eq(dictItemDTO.getId() != null, DictItem::getId, dictItemDTO.getId());

        update(wrapper);
    }

    @Override
    public void deleteDictItem(List<Long> dictItemIds) {
        removeByIds(dictItemIds);
    }

    /**
     * 校验字典项标签是否唯一
     * @return
     */
    private boolean checkDictItemLabelUnique(String typeCode, String label) {
//        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(StringUtils.isNotEmpty(label),  DictItem::getLabel, label);
//        List<DictItem> dictItemList = dictItemMapper.selectList(wrapper);
//        if ( dictItemList == null) {
//            return true;
//        }
//        if (dictItemList.size() == 1) {
//            return dictItemList.get(0).getLabel());
//        }
        return false;
    }

    /**
     * 校验字典项值是否唯一
     * @return
     */
    private boolean checkDictItemValueUnique(String typeCode, String value) {
        return false;
    }
}
