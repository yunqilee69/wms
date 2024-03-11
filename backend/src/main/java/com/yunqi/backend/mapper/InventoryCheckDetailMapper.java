package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.InventoryCheckDetailDTO;
import com.yunqi.backend.model.entity.InventoryCheck;
import com.yunqi.backend.model.entity.InventoryCheckDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点细节mapper
 * @author liyunqi
 */
@Mapper
public interface InventoryCheckDetailMapper extends BaseMapper<InventoryCheckDetail> {

    List<InventoryCheckDetailDTO> getDetailPage(@Param("detailDTO") InventoryCheckDetailDTO detailDTO);

    /**
     * 根据id获取DTO
     * @param checkDetailId
     * @return
     */
    InventoryCheckDetailDTO selectCheckDetailById(Long checkDetailId);
}
