package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.OrderPurchase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单采购Mapper
 * @author liyunqi
 */
@Mapper
public interface OrderPurchaseMapper extends BaseMapper<OrderPurchase> {
}
