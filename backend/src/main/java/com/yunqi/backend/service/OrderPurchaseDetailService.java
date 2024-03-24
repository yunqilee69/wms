package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.OrderPurchaseDetailDTO;
import com.yunqi.backend.model.entity.OrderPurchaseDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单采购服务接口
 * @author liyunqi
 */
public interface OrderPurchaseDetailService extends IService<OrderPurchaseDetail> {

    /**
     * 分页查询
     * @param orderPurchaseDTO
     * @return
     */
    Page<OrderPurchaseDetail> getOrderPurchaseDetailPage(OrderPurchaseDetailDTO orderPurchaseDetailDTO);

    /**
     * 添加货物到订单
     * @param orderId
     * @param recordIds
     * @param detailType
     */
    void addUnPurchaseRecord(Long orderId, Long[] recordIds, String detailType);

    /**
     * 设置细节表中的货物的数量
     * @param detailIds
     * @param number
     */
    void setNumber(Long[] detailIds, int number);

    /**
     * 删除货物
     * @param orderPurchaseDetailIds
     */
    void deleteByIds(List<Long> orderPurchaseDetailIds);

    /**
     * 设置细节表中的货物的价格
     * @param detailIds
     * @param amount
     */
    void setAmount(Long[] detailIds, BigDecimal amount);
}
