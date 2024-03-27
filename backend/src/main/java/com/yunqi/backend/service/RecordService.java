package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.Record;

import java.time.LocalDateTime;
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

    /**
     * 获取采购订单中未被加入的货物记录
     * @param recordDTO
     * @param orderId
     * @return
     */
    Page<RecordDTO> getUnPurchaseRecordList(RecordDTO recordDTO, Long orderId);

    /**
     * 获取销售订单中未被加入的货物记录
     * @param recordDTO
     * @param orderId
     * @return
     */
    Page<RecordDTO> getUnSaleRecordList(RecordDTO recordDTO, Long orderId);

    /**
     * 新增库存记录
     * @param recordDTO
     */
    void saveRecord(RecordDTO recordDTO);

    /**
     * 根据货物id获取库存记录数据
     * @param wareId
     * @return
     */
    List<Record> getRecordByWareId(Long wareId);

    /**
     * 获取指定日期内的库存总数量
     * @param begin
     * @param end
     * @return
     */
    int getAllNumber(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取在库数量低于报警阈值的所有库存记录
     * @return
     */
    List<RecordDTO> getAlarmRecord();
}
