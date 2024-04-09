package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.OrderSaleDetailDTO;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.OrderSaleDetail;
import com.yunqi.backend.service.OrderSaleDetailService;
import com.yunqi.backend.service.RecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单销售控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/order/saleDetail")
public class OrderSaleDetailController {

    @Resource
    private OrderSaleDetailService orderSaleDetailService;

    @Resource
    RecordService recordService;

    /**
     * 分页查询
     * @param orderSaleDetailDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:query')")
    @GetMapping("/list")
    public Result getList(OrderSaleDetailDTO orderSaleDetailDTO) {
        Page<OrderSaleDetail> page = orderSaleDetailService.getOrderSaleDetailPage(orderSaleDetailDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param orderSaleDetailId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:query')")
    @GetMapping("/{orderSaleDetailId}")
    public Result getOne(@PathVariable Long orderSaleDetailId) {
        OrderSaleDetail orderSaleDetail = orderSaleDetailService.getById(orderSaleDetailId);
        return Result.success(orderSaleDetail);
    }

    /**
     * 删除
     * @param orderSaleDetailIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:edit')")
    @DeleteMapping("/{orderSaleDetailIds}")
    public Result delete(@PathVariable List<Long> orderSaleDetailIds) {
        if (orderSaleDetailIds == null || orderSaleDetailIds.size() == 0) {
            return Result.fail("销售订单id不能为空");
        }
        orderSaleDetailService.deleteByIds(orderSaleDetailIds);
        return Result.success();
    }

    /**
     * 设置细节表中的货物的数量
     * @param detailIds
     * @param number
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:edit')")
    @PutMapping("/setNumber")
    public Result setNumber(Long[] detailIds, Integer number) {
        orderSaleDetailService.setNumber(detailIds, number);
        return Result.success();
    }

    /**
     * 设置细节表中的货物的价格
     * @param detailIds
     * @param number
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:edit')")
    @PutMapping("/setAmount")
    public Result setAmount(Long[] detailIds, BigDecimal amount) {
        orderSaleDetailService.setAmount(detailIds, amount);
        return Result.success();
    }

    /**
     * 获取该订单中还未选择的库存记录
     * @param recordDTO
     * @param checkId
     * @return
     */
    @GetMapping("/getUnSaleRecordList")
    public Result getUnSaleRecordList(RecordDTO recordDTO, Long orderId, String detailType) {
        if (detailType != null && detailType.equals("退货")) {
            // 退货需要查询所有的库存记录
            orderId = 0L;
        }
        Page<RecordDTO> page = recordService.getUnSaleRecordList(recordDTO, orderId);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 添加货物记录
     * @param orderId
     * @param recordIds
     * @param detailType
     * @return
     */
    @PreAuthorize("@sps.hasPermi('order:sale:edit')")
    @PutMapping("/addUnSaleRecord")
    public Result addUnSaleRecord(Long orderId, Long[] recordIds, String detailType) {
        orderSaleDetailService.addUnSaleRecord(orderId, recordIds, detailType);
        return Result.success();
    }
}
