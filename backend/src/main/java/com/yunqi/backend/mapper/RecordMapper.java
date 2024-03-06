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
}
