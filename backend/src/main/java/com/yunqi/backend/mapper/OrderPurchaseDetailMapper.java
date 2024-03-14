package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.OrderPurchase;
import com.yunqi.backend.model.entity.OrderPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单采购Mapper
 * @author liyunqi
 */
@Mapper
public interface OrderPurchaseDetailMapper extends BaseMapper<OrderPurchaseDetail> {
}
