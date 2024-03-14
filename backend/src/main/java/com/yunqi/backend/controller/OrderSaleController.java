package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.OrderSaleDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.entity.OrderSale;
import com.yunqi.backend.service.OrderSaleService;
import com.yunqi.backend.service.OrderSettlementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单销售控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/order/sale")
public class OrderSaleController {

    @Resource
    private OrderSaleService orderSaleService;

    @Resource
    OrderSettlementService orderSettlementService;

    /**
     * 分页查询
     * @param orderSaleDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('sale:order:list')")
    @GetMapping("/list")
    public Result getList(OrderSaleDTO orderSaleDTO) {
        Page<OrderSale> page = orderSaleService.getOrderSalePage(orderSaleDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param orderSaleId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('sale:order:query')")
    @GetMapping("/{orderSaleId}")
    public Result getOne(@PathVariable Long orderSaleId) {
        OrderSale orderSale = orderSaleService.getById(orderSaleId);
        return Result.success(orderSale);
    }

    /**
     * 新增
     * @param supplierId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('sale:order:add')")
    @PostMapping
    public Result add(@RequestBody Long supplierId) {
        orderSaleService.saveOrderSale(supplierId);
        return Result.success();
    }

    /**
     * 确认收货
     * @param orderId
     * @return
     */
    @PutMapping("/takeDelivery")
    public Result takeDelivery(Long orderId) {
        orderSaleService.takeDelivery(orderId);
        return Result.success();
    }

    /**
     * 订单结算
     * @param settlementDTO
     * @return
     */
    @PutMapping("/settlement")
    public Result settlement(SettlementDTO settlementDTO) {
        orderSaleService.settlementOrder(settlementDTO);
        return Result.success();
    }

    /**
     * 获取订单的支付截图
     * @param orderId
     * @return
     */
    @GetMapping("/pictures")
    public Result pictures(Long orderId) {
        List<String> picturesList = orderSettlementService.getPicturesByOrderId(orderId);
        return Result.success(String.join(",", picturesList));
    }

    /**
     * 删除
     * @param orderSaleIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('sale:order:delete')")
    @DeleteMapping("/{orderSaleIds}")
    public Result delete(@PathVariable List<Long> orderSaleIds) {
        if (orderSaleIds == null || orderSaleIds.size() == 0) {
            return Result.fail("销售订单id不能为空");
        }
        orderSaleService.deleteByIds(orderSaleIds);
        return Result.success();
    }

}
