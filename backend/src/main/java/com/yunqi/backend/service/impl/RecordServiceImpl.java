package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.RecordError;
import com.yunqi.backend.mapper.LocationMapper;
import com.yunqi.backend.mapper.RecordMapper;
import com.yunqi.backend.mapper.WareMapper;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.Location;
import com.yunqi.backend.model.entity.OrderSale;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.RecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Resource
    RecordMapper recordMapper;

    @Resource
    LocationMapper locationMapper;

    @Resource
    WareMapper wareMapper;

    @Value("${wms.alarmMonth}")
    Integer alarmMonth;

    @Override
    public Page<RecordDTO> getRecordPage(RecordDTO recordDTO) {
        Page<RecordDTO> page = PageUtils.getPage();
        List<RecordDTO> recordDTOList = recordMapper.getRecordPage(recordDTO);
        PageUtils.handlePageList(recordDTOList, page);
        return page;
    }

    @Override
    public void updateAlarmThreshold(List<Long> recordIds, Integer alarmThreshold) {
        if (recordIds == null || recordIds.size() < 1  ) {
            throw new BizException(RecordError.RECORD_ID_IS_EMPTY);
        }
        if (alarmThreshold == null || alarmThreshold < 0) {
            throw new BizException(RecordError.ALARM_THRESHOLD_LT_0);
        }

        LambdaUpdateWrapper<Record> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Record::getAlarmThreshold, alarmThreshold);
        wrapper.in(Record::getId, recordIds);
        recordMapper.update(wrapper);
    }

    @Override
    public Page<RecordDTO> getUnCheckRecordList(RecordDTO recordDTO, Long checkId) {
        Page<RecordDTO> page = PageUtils.getPage();
        List<RecordDTO> recordDTOList = recordMapper.getUnCheckRecordPage(recordDTO, checkId);
        PageUtils.handlePageList(recordDTOList, page);
        return page;
    }

    @Override
    public Page<RecordDTO> getUnPurchaseRecordList(RecordDTO recordDTO, Long orderId) {
        Page<RecordDTO> page = PageUtils.getPage();
        List<RecordDTO> recordDTOList = recordMapper.getUnPurchaseRecordList(recordDTO, orderId);
        PageUtils.handlePageList(recordDTOList, page);
        return page;
    }

    @Override
    public Page<RecordDTO> getUnSaleRecordList(RecordDTO recordDTO, Long orderId) {
        Page<RecordDTO> page = PageUtils.getPage();
        List<RecordDTO> recordDTOList = recordMapper.getUnSaleRecordList(recordDTO, orderId);
        PageUtils.handlePageList(recordDTOList, page);
        return page;
    }

    @Override
    public void saveRecord(RecordDTO recordDTO) {
        Record record = new Record();
        BeanUtils.copyProperties(recordDTO, record);

        // 组装数据
        Location location = locationMapper.selectById(recordDTO.getLocationId());
        Ware ware = wareMapper.selectById(recordDTO.getWareId());
        record.setLocationName(location.getName());
        record.setQualityMonth(ware.getQualityMonth());
        record.setGuaranteeDate(recordDTO.getProductionDate().plusMonths(ware.getQualityMonth()));
        record.setTotalAmount(BigDecimal.ZERO);

        recordMapper.insert(record);
    }

    @Override
    public List<Record> getRecordByWareId(Long wareId) {
        if (wareId == null) {
            return null;
        }
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Record::getWareId, wareId);
        return recordMapper.selectList(wrapper);
    }

    @Override
    public int getAllNumber(LocalDateTime begin, LocalDateTime end) {
        return 0;
    }

    @Override
    public List<RecordDTO> getAlarmRecord() {
        return recordMapper.getAlarmRecord();
    }

    @Override
    public List<RecordDTO> getAlarmExp() {
        return recordMapper.getAlarmExp(alarmMonth);
    }
}
