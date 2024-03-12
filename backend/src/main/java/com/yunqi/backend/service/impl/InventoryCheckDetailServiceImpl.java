package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.InventoryCheckError;
import com.yunqi.backend.mapper.InventoryCheckDetailMapper;
import com.yunqi.backend.model.dto.InventoryCheckDetailDTO;
import com.yunqi.backend.model.entity.InventoryCheckDetail;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.service.InventoryCheckDetailService;
import com.yunqi.backend.service.RecordService;
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
public class InventoryCheckDetailServiceImpl extends ServiceImpl<InventoryCheckDetailMapper, InventoryCheckDetail> implements InventoryCheckDetailService {

    @Resource
    InventoryCheckDetailMapper inventoryCheckDetailMapper;

    @Resource
    RecordService recordService;

    @Override
    public Page<InventoryCheckDetailDTO> getInventoryCheckDetailPage(InventoryCheckDetailDTO inventoryCheckDetailDTO) {
        Page<InventoryCheckDetailDTO> page = PageUtils.getPage();
        List<InventoryCheckDetailDTO> list = inventoryCheckDetailMapper.getDetailPage(inventoryCheckDetailDTO);
        PageUtils.handlePageList(list, page);
        return page;
    }

    @Override
    public void saveInventoryCheckDetail(InventoryCheckDetailDTO inventoryCheckDetailDTO) {
        InventoryCheckDetail inventoryCheckDetail = new InventoryCheckDetail();
        BeanUtils.copyProperties(inventoryCheckDetailDTO, inventoryCheckDetail);
        // TODO 库存盘点细节 新增校验
        inventoryCheckDetailMapper.insert(inventoryCheckDetail);
    }

    @Override
    public void updateInventoryCheckDetail(InventoryCheckDetailDTO inventoryCheckDetailDTO) {
        // 状态为已盘点的不能进行更新
        LambdaUpdateWrapper<InventoryCheckDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InventoryCheckDetail::getId, inventoryCheckDetailDTO.getId());
        wrapper.set(InventoryCheckDetail::getPreNumber, inventoryCheckDetailDTO.getPreNumber());
        wrapper.set(InventoryCheckDetail::getNowNumber, inventoryCheckDetailDTO.getNowNumber());
        wrapper.set(InventoryCheckDetail::getPreProductionDate, inventoryCheckDetailDTO.getPreProductionDate());
        wrapper.set(InventoryCheckDetail::getNowProductionDate, inventoryCheckDetailDTO.getNowProductionDate());
        inventoryCheckDetailMapper.update(wrapper);
    }

    @Override
    public InventoryCheckDetailDTO selectById(Long checkDetailId) {
        return inventoryCheckDetailMapper.selectCheckDetailById(checkDetailId);
    }

    @Override
    public void addCheckRecordAll(Long checkId, Long[] recordIds) {
        for (Long recordId : recordIds) {
            Record record = recordService.getById(recordId);
            InventoryCheckDetail inventoryCheckDetail = new InventoryCheckDetail();
            inventoryCheckDetail.setCheckId(checkId);
            inventoryCheckDetail.setRecordId(recordId);
            inventoryCheckDetail.setPreNumber(record.getNumber());
            inventoryCheckDetail.setNowNumber(record.getNumber());
            inventoryCheckDetail.setPreProductionDate(record.getProductionDate());
            inventoryCheckDetail.setNowProductionDate(record.getProductionDate());
            inventoryCheckDetailMapper.insert(inventoryCheckDetail);
        }
    }
}
