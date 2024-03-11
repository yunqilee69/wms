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

    /**
     * 获取在这次的盘点过程中还未盘点的库存记录
     * @param recordDTO
     * @param checkId
     * @return
     */
    Page<RecordDTO> getUnCheckRecordList(RecordDTO recordDTO, Long checkId);
}
