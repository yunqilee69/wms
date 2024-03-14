package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存记录Mapper
 * @author liyunqi
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    /**
     * 分页查询
     * @param recordDTO
     * @return
     */
    List<RecordDTO> getRecordPage(@Param("recordDTO") RecordDTO recordDTO);

    /**
     * 获取在这次的盘点过程中还未盘点的库存记录
     * @param recordDTO
     * @param checkId
     * @return
     */
    List<RecordDTO> getUnCheckRecordPage(@Param("recordDTO") RecordDTO recordDTO, @Param("checkId") Long checkId);

    /**
     * 获取采购订单中未被加入的货物记录
     * @param recordDTO
     * @param orderId
     * @return
     */
    List<RecordDTO> getUnPurchaseRecordList(@Param("recordDTO") RecordDTO recordDTO, @Param("orderId")  Long orderId);
}
