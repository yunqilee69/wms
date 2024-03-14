package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.OrderPurchaseDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.OrderPurchase;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.OrderPurchaseService;
import com.yunqi.backend.service.OrderSettlementService;
import com.yunqi.backend.service.WareService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单采购控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/order/purchase")
public class OrderPurchaseController {

    @Resource
    private OrderPurchaseService orderPurchaseService;

    @Resource
    OrderSettlementService orderSettlementService;

    /**
     * 分页查询
     * @param orderPurchaseDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:list')")
    @GetMapping("/list")
    public Result getList(OrderPurchaseDTO orderPurchaseDTO) {
        Page<OrderPurchase> page = orderPurchaseService.getOrderPurchasePage(orderPurchaseDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param orderPurchaseId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:query')")
    @GetMapping("/{orderPurchaseId}")
    public Result getOne(@PathVariable Long orderPurchaseId) {
        OrderPurchase orderPurchase = orderPurchaseService.getById(orderPurchaseId);
        return Result.success(orderPurchase);
    }

    /**
     * 新增
     * @param supplierId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:add')")
    @PostMapping
    public Result add(@RequestBody Long supplierId) {
        orderPurchaseService.saveOrderPurchase(supplierId);
        return Result.success();
    }

    /**
     * 确认收货
     * @param orderId
     * @return
     */
    @PutMapping("/takeDelivery")
    public Result takeDelivery(Long orderId) {
        orderPurchaseService.takeDelivery(orderId);
        return Result.success();
    }

    /**
     * 订单结算
     * @param settlementDTO
     * @return
     */
    @PutMapping("/settlement")
    public Result settlement(SettlementDTO settlementDTO) {
        orderPurchaseService.settlementOrder(settlementDTO);
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
     * @param orderPurchaseIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:delete')")
    @DeleteMapping("/{orderPurchaseIds}")
    public Result delete(@PathVariable List<Long> orderPurchaseIds) {
        if (orderPurchaseIds == null || orderPurchaseIds.size() == 0) {
            return Result.fail("采购订单id不能为空");
        }
        orderPurchaseService.deleteByIds(orderPurchaseIds);
        return Result.success();
    }

}
