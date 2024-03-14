package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.OrderSaleDetailDTO;
import com.yunqi.backend.model.entity.OrderSaleDetail;

import java.util.List;

/**
 * 订单采购服务接口
 * @author liyunqi
 */
public interface OrderSaleDetailService extends IService<OrderSaleDetail> {

    /**
     * 分页查询
     * @param orderSaleDTO
     * @return
     */
    Page<OrderSaleDetail> getOrderSaleDetailPage(OrderSaleDetailDTO orderSaleDetailDTO);

    /**
     * 添加货物到订单
     * @param orderId
     * @param recordIds
     * @param detailType
     */
    void addUnSaleRecord(Long orderId, Long[] recordIds, String detailType);

    /**
     * 设置细节表中的货物的数量
     * @param detailIds
     * @param number
     */
    void setNumber(Long[] detailIds, int number);

    /**
     * 删除货物
     * @param orderSaleDetailIds
     */
    void deleteByIds(List<Long> orderSaleDetailIds);
}
