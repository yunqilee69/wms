package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.entity.OrderSettlement;

import java.util.List;

/**
 * 订单支付记录service
 * @author liyunqi
 */
public interface OrderSettlementService extends IService<OrderSettlement> {

    /**
     * 保存支付截图
     * @param orderId
     * @param pictureList
     */
    void savePicture(Long orderId, List<String> pictureList);

    /**
     * 获取支付截图
     * @param orderId
     * @return
     */
    List<String> getPicturesByOrderId(Long orderId);

}
