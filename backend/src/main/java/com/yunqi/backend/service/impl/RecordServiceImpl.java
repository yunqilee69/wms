package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.RecordError;
import com.yunqi.backend.mapper.RecordMapper;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.service.RecordService;
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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Resource
    RecordMapper recordMapper;

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
}
