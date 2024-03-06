package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.Record;

import java.util.List;

/**
 * @author liyunqi
 */
public interface RecordService extends IService<Record> {
    /**
     * 分页查询
     * @param recordDTO
     * @return
     */
    Page<RecordDTO> getRecordPage(RecordDTO recordDTO);

    /**
     * 更新报警阈值
     * @param recordIds
     * @param alarmThreshold
     */
    void updateAlarmThreshold(List<Long> recordIds, Integer alarmThreshold);
}
