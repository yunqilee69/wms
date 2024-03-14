package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.OrderPurchaseDTO;
import com.yunqi.backend.model.dto.OrderPurchaseDetailDTO;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.entity.OrderPurchase;
import com.yunqi.backend.model.entity.OrderPurchaseDetail;
import com.yunqi.backend.service.OrderPurchaseDetailService;
import com.yunqi.backend.service.RecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单采购控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/order/purchaseDetail")
public class OrderPurchaseDetailController {

    @Resource
    private OrderPurchaseDetailService orderPurchaseDetailService;

    @Resource
    RecordService recordService;

    /**
     * 分页查询
     * @param orderPurchaseDetailDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:list')")
    @GetMapping("/list")
    public Result getList(OrderPurchaseDetailDTO orderPurchaseDetailDTO) {
        Page<OrderPurchaseDetail> page = orderPurchaseDetailService.getOrderPurchaseDetailPage(orderPurchaseDetailDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param orderPurchaseDetailId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:query')")
    @GetMapping("/{orderPurchaseDetailId}")
    public Result getOne(@PathVariable Long orderPurchaseDetailId) {
        OrderPurchaseDetail orderPurchaseDetail = orderPurchaseDetailService.getById(orderPurchaseDetailId);
        return Result.success(orderPurchaseDetail);
    }

    /**
     * 删除
     * @param orderPurchaseDetailIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('purchase:order:delete')")
    @DeleteMapping("/{orderPurchaseDetailIds}")
    public Result delete(@PathVariable List<Long> orderPurchaseDetailIds) {
        if (orderPurchaseDetailIds == null || orderPurchaseDetailIds.size() == 0) {
            return Result.fail("采购订单id不能为空");
        }
        orderPurchaseDetailService.deleteByIds(orderPurchaseDetailIds);
        return Result.success();
    }

    /**
     * 设置细节表中的货物的数量
     * @param detailIds
     * @param number
     * @return
     */
    @PutMapping("/setNumber")
    public Result setNumber(Long[] detailIds, Integer number) {
        orderPurchaseDetailService.setNumber(detailIds, number);
        return Result.success();
    }

    /**
     * 获取该订单中还未选择的库存记录
     * @param recordDTO
     * @param checkId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:query')")
    @GetMapping("/getUnPurchaseRecordList")
    public Result getUnPurchaseRecordList(RecordDTO recordDTO, Long orderId, String detailType) {
        if (detailType != null && detailType.equals("退货")) {
            // 退货需要查询所有的库存记录
            orderId = 0L;
        }
        Page<RecordDTO> page = recordService.getUnPurchaseRecordList(recordDTO, orderId);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 添加货物记录
     * @param orderId
     * @param recordIds
     * @param detailType
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @PutMapping("/addUnPurchaseRecord")
    public Result addUnPurchaseRecord(Long orderId, Long[] recordIds, String detailType) {
        orderPurchaseDetailService.addUnPurchaseRecord(orderId, recordIds, detailType);
        return Result.success();
    }
}
