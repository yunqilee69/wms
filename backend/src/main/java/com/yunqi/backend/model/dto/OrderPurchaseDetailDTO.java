package com.yunqi.backend.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购订单细节DTO
 * @author liyunqi
 */
@Data
public class OrderPurchaseDetailDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 货物id
     */
    private Long wareId;

    /**
     * 库存记录id
     */
    private Long recordId;

    /**
     * 货物图片
     */
    private String picture;

    /**
     * 货位名称
     */
    private String locationName;

    /**
     * 货物名称
     */
    private String wareName;

    /**
     * 货物品牌
     */
    private String wareBrand;

    /**
     * 货物规格
     */
    private String wareSpec;

    /**
     * 货物单位
     */
    private String wareUnit;

    /**
     * 货物条形码
     */
    private String wareBarCode;


    /**
     * 货物进价
     */
    private BigDecimal warePurchasePrice;

    /**
     * 货物数量
     */
    private int wareNumber;

    /**
     * 类型，进货/退货
     */
    private String type;
}
