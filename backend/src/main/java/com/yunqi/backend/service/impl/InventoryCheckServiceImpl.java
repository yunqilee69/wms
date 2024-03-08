package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.constant.DocumentConstants;
import com.yunqi.backend.common.util.DocumentUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.InventoryCheckError;
import com.yunqi.backend.mapper.InventoryCheckMapper;
import com.yunqi.backend.model.dto.InventoryCheckDTO;
import com.yunqi.backend.model.entity.InventoryCheck;
import com.yunqi.backend.service.InventoryCheckService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheck> implements InventoryCheckService {

    @Resource
    InventoryCheckMapper inventoryCheckMapper;

    @Override
    public Page<InventoryCheck> getInventoryCheckPage(InventoryCheckDTO inventoryCheckDTO) {
        Page<InventoryCheck> page = PageUtils.getPage();
        LambdaQueryWrapper<InventoryCheck> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(inventoryCheckDTO.getDocumentCode() != null, InventoryCheck::getDocumentCode, inventoryCheckDTO.getDocumentCode());
        wrapper.like(inventoryCheckDTO.getName() != null, InventoryCheck::getName, inventoryCheckDTO.getName());
        wrapper.eq(inventoryCheckDTO.getType() != null, InventoryCheck::getType, inventoryCheckDTO.getType());
        wrapper.eq(inventoryCheckDTO.getStatus() != null, InventoryCheck::getStatus, inventoryCheckDTO.getStatus());
        return inventoryCheckMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveInventoryCheck(InventoryCheckDTO inventoryCheckDTO) {
        InventoryCheck inventoryCheck = new InventoryCheck();
        BeanUtils.copyProperties(inventoryCheckDTO, inventoryCheck);
        // TODO 库存盘点 新增校验
        String documentCode = DocumentUtils.generateDocumentNumber(DocumentConstants.CHECK);
        inventoryCheck.setDocumentCode(documentCode);
        inventoryCheckMapper.insert(inventoryCheck);
    }

    @Override
    public void updateInventoryCheck(InventoryCheckDTO inventoryCheckDTO) {
        // 状态为已盘点的不能进行更新
        InventoryCheck inventoryCheck = inventoryCheckMapper.selectById(inventoryCheckDTO.getId());
        if (inventoryCheck == null || inventoryCheck.getStatus().equals("3")) {
            // 状态为已修正，不允许进行删除
            throw new BizException(InventoryCheckError.STATUS_IS_FIXED_UPDATE);
        }

        LambdaUpdateWrapper<InventoryCheck> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(InventoryCheck::getName, inventoryCheckDTO.getName());
        wrapper.set(InventoryCheck::getRemark, inventoryCheckDTO.getRemark());
        wrapper.eq(InventoryCheck::getId, inventoryCheckDTO.getId());
        inventoryCheckMapper.update(wrapper);
    }

    @Override
    public void deleteInventoryCheck(List<Long> ids) {
        for (Long id : ids) {
            InventoryCheck inventoryCheck = inventoryCheckMapper.selectById(id);
            if (inventoryCheck == null || inventoryCheck.getStatus().equals("3")) {
                // 状态为已修正，不允许进行删除
                throw new BizException(InventoryCheckError.STATUS_IS_FIXED_DEL);
            }
        }
        removeBatchByIds(ids);
    }
}
