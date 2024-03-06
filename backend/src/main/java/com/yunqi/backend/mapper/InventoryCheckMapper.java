package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.InventoryCheck;
import org.apache.ibatis.annotations.Mapper;

/**
 * 盘点mapper
 * @author liyunqi
 */
@Mapper
public interface InventoryCheckMapper extends BaseMapper<InventoryCheck> {
}
