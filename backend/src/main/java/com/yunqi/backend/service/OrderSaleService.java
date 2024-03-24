package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.OrderSaleDTO;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.entity.OrderSale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单销售服务接口
 * @author liyunqi
 */
public interface OrderSaleService extends IService<OrderSale> {

    /**
     * 分页查询
     * @param orderSaleDTO
     * @return
     */
    Page<OrderSale> getOrderSalePage(OrderSaleDTO orderSaleDTO);

    /**
     * 保存
     *
     * @param supplierId
     */
    void saveOrderSale(Long supplierId);

    /**
     * 确认送货
     * @param orderId
     */
    void takeDelivery(Long orderId);

    /**
     * 结算订单
     *
     * @param settlementDTO
     */
    void settlementOrder(SettlementDTO settlementDTO);

    /**
     * 删除销售单
     * @param orderSaleIds
     */
    void deleteByIds(List<Long> orderSaleIds);

    /**
     * 更新订单的基础数据，在新增货物或设置货物数量后使用
     * @param orderId
     */
    void updateOrderSaleData(Long orderId);

    /**
     * 根据时间范围获取销售订单的总实际金额
     * @param begin
     * @param end
     * @return
     */
    BigDecimal getAmountByDate(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据时间范围获取销售订单的销量top10
     * @param begin
     * @param end
     * @return
     */
    List<Map<String, String>> getSalesTop10(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据时间范围获取销售订单的利润top10
     * @param begin
     * @param end
     * @return
     */
    List<Map<String, String>> getProfitTop10(LocalDateTime begin, LocalDateTime end);
}
