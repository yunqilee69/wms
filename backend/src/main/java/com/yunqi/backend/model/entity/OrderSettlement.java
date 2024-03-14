package com.yunqi.backend.model.entity;

import lombok.Data;

/**
 * 订单支付截图
 * @author liyunqi
 */
@Data
public class OrderSettlement {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 图片url
     */
    private String picture;

}
