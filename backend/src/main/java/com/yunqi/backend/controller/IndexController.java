package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.model.entity.OrderPurchase;
import com.yunqi.backend.model.entity.OrderSale;
import com.yunqi.backend.service.OrderPurchaseService;
import com.yunqi.backend.service.OrderSaleService;
import com.yunqi.backend.service.RecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    RecordService recordService;

    @Resource
    OrderPurchaseService purchaseService;

    @Resource
    OrderSaleService saleService;

    /**
     * 获取处于报警阈值之下的库存记录
     * @return
     */
    @GetMapping("/getAlarmRecord")
    public Result getAlarmRecord() {
        return Result.success(recordService.getAlarmRecord());
    }

    /**
     * 获取订单状态为已收款或已付款的订单
     * @return
     */
    @GetMapping("/getAlarmOrder")
    public Result getAlarmOrder() {
        // 获取数据
        LambdaQueryWrapper<OrderPurchase> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(OrderPurchase::getStatus, DictUtils.getValueByLabel("已收货", "purchase_order_status"));
        List<OrderPurchase> purchaseList = purchaseService.getBaseMapper().selectList(wrapper1);

        LambdaQueryWrapper<OrderSale> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(OrderSale::getStatus, DictUtils.getValueByLabel("已送货", "sale_order_status"));
        List<OrderSale> saleList = saleService.getBaseMapper().selectList(wrapper2);

        // 组装数据
        List<Map<String, Object>> result = new ArrayList<>();
        purchaseList.stream().forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("documentCode", item.getDocumentCode());
            map.put("orderName", "采购单");
            map.put("amount", item.getOriginAmount().subtract(item.getReturnAmount()));
            map.put("wareTime", item.getReceiptTime());
            result.add(map);
        });
        saleList.stream().forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("documentCode", item.getDocumentCode());
            map.put("orderName", "销售单");
            map.put("amount", item.getOriginAmount().subtract(item.getReturnAmount()));
            map.put("wareTime", item.getDeliveryTime());
            result.add(map);
        });
        return Result.success(result);
    }

    /**
     * 获取处于报警阈值之下的库存记录
     * @return
     */
    @GetMapping("/getAlarmExp")
    public Result getAlarmExp() {
        return Result.success(recordService.getAlarmExp());
    }
}
