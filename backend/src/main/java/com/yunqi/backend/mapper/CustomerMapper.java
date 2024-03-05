package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户mapper
 * @author liyunqi
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
