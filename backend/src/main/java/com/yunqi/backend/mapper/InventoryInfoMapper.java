package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.Customer;
import com.yunqi.backend.model.entity.InventoryInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息mapper
 * @author liyunqi
 */
@Mapper
public interface InventoryInfoMapper extends BaseMapper<InventoryInfo> {
}
