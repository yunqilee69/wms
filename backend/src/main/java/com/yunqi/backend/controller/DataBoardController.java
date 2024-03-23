package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.service.InventoryInfoService;
import com.yunqi.backend.service.OrderPurchaseService;
import com.yunqi.backend.service.OrderSaleService;
import com.yunqi.backend.service.RecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 数据看板数据展示
 * @author liyunqi
 */
@RestController
@RequestMapping("/dataBoard")
public class DataBoardController {

    @Resource
    OrderPurchaseService purchaseService;

    @Resource
    OrderSaleService saleService;

    @Resource
    RecordService recordService;

    @Resource
    InventoryInfoService inventoryInfoService;

    /**
     * 获取指定日期的采购额
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/purchaseMoney")
    public Result purchaseMoney(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        List<LocalDate> dateList = getAllDate(begin, end);
        List<BigDecimal> amountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 获取该天的金额
            BigDecimal amount = purchaseService.getAmountByDate(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            amountList.add(amount);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", dateList);
        result.put("yAxis", amountList);
        return Result.success(result);
    }

    /**
     * 获取指定日期的销售额
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/saleMoney")
    public Result saleMoney(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        List<LocalDate> dateList = getAllDate(begin, end);
        List<BigDecimal> amountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 获取该天的金额
            BigDecimal amount = saleService.getAmountByDate(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            amountList.add(amount);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", dateList);
        result.put("yAxis", amountList);
        return Result.success(result);
    }
    
    /**
     * 获取指定日期的库存货物总数量
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/inventoryNumber")
    public Result inventoryNumber(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        List<LocalDate> dateList = getAllDate(begin, end);
        List<Integer> numberList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 获取该天的库存总数量
            int number = inventoryInfoService.getNumberByDate(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            numberList.add(number);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", dateList);
        result.put("yAxis", numberList);
        return Result.success(result);
    }

    /**
     * 获取指定日期的库存货物总价值
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/inventoryAmount")
    public Result inventoryAmount(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        List<LocalDate> dateList = getAllDate(begin, end);
        List<BigDecimal> amountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 获取该天的库存总数量
            BigDecimal amount = inventoryInfoService.getAmountByDate(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            amountList.add(amount);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", dateList);
        result.put("yAxis", amountList);
        return Result.success(result);
    }

    /**
     * 分析订单，生成销量top10的货物
     */
    @GetMapping("/salesTop10")
    public Result salesTop10() {
        return Result.success();
    }

    /**
     *  分析订单，生成利润top10的货物
     */
    @GetMapping("/profitTop10")
    public Result profitTop10() {
        return Result.success();
    }

    // 获取两个日期之间的所有日期
    private List<LocalDate> getAllDate(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        return dateList;
    }

}
