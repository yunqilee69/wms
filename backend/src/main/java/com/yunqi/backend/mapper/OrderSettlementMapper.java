package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.OrderSettlementDTO;
import com.yunqi.backend.model.entity.OrderSettlement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单支付记录Mapper
 * @author liyunqi
 */
@Mapper
public interface OrderSettlementMapper extends BaseMapper<OrderSettlement> {
}
