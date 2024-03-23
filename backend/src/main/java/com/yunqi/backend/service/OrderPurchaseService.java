package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.OrderPurchaseDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.entity.OrderPurchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单采购服务接口
 * @author liyunqi
 */
public interface OrderPurchaseService extends IService<OrderPurchase> {

    /**
     * 分页查询
     * @param orderPurchaseDTO
     * @return
     */
    Page<OrderPurchase> getOrderPurchasePage(OrderPurchaseDTO orderPurchaseDTO);

    /**
     * 保存
     *
     * @param supplierId
     */
    void saveOrderPurchase(Long supplierId);

    /**
     * 确认收货
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
     * 删除采购单
     * @param orderPurchaseIds
     */
    void deleteByIds(List<Long> orderPurchaseIds);

    /**
     * 更新订单的基础数据，在新增货物或设置货物数量后使用
     * @param orderId
     */
    void updateOrderPurchaseData(Long orderId);

    /**
     * 根据时间范围获取采购订单的总实际金额
     * @param begin
     * @param end
     * @return
     */
    BigDecimal getAmountByDate(LocalDateTime begin, LocalDateTime end);
}
