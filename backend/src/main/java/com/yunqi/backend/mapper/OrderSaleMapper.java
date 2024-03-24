package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.OrderSale;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单销售Mapper
 * @author liyunqi
 */
@Mapper
public interface OrderSaleMapper extends BaseMapper<OrderSale> {
    // 获取时间范围内的销量top10
    @MapKey("wareName")
    List<Map<String, String>> getSalesTop10(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    // 获取时间范围内的利润top10
    @MapKey("wareName")
    List<Map<String, String>> getProfitTop10(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}
