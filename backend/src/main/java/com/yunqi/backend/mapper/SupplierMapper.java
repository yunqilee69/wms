package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商mapper
 * @author liyunqi
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
}
