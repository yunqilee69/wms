package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.InventoryCheckDetailDTO;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.InventoryCheckDetail;
import com.yunqi.backend.service.InventoryCheckDetailService;
import com.yunqi.backend.service.RecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点库
 * @author liyunqi
 */
@RestController
@RequestMapping("/inventory/checkDetail")
public class InventoryCheckDetailController {

    @Resource
    InventoryCheckDetailService inventoryCheckDetailService;

    @Resource
    RecordService recordService;

    /**
     * 分页查询
     * @param inventoryCheckDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:query')")
    @GetMapping("/list")
    public Result getList(InventoryCheckDetailDTO inventoryCheckDTO) {
        Page<InventoryCheckDetail> page = inventoryCheckDetailService.getInventoryCheckDetailPage(inventoryCheckDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param inventoryCheckId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:query')")
    @GetMapping("/{inventoryCheckId}")
    public Result getOne(@PathVariable Long inventoryCheckId) {
        InventoryCheckDetailDTO detailDTO = inventoryCheckDetailService.selectById(inventoryCheckId);
        return Result.success(detailDTO);
    }

    /**
     * 新增
     * @param inventoryCheckDetailDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @PostMapping
    public Result add(@RequestBody InventoryCheckDetailDTO inventoryCheckDetailDTO) {
        inventoryCheckDetailService.saveInventoryCheckDetail(inventoryCheckDetailDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param inventoryCheckDetailIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @DeleteMapping("/{inventoryCheckDetailIds}")
    public Result delete(@PathVariable List<Long> inventoryCheckDetailIds) {
        if (inventoryCheckDetailIds == null || inventoryCheckDetailIds.size() == 0) {
            return Result.fail("库存盘点细节id不能为空");
        }
        inventoryCheckDetailService.removeBatchByIds(inventoryCheckDetailIds);
        return Result.success();
    }

    /**
     * 更新
     * @param inventoryCheckDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @PutMapping
    public Result update(@RequestBody InventoryCheckDetailDTO inventoryCheckDTO) {
        inventoryCheckDetailService.updateInventoryCheckDetail(inventoryCheckDTO);
        return Result.success();
    }

    /**
     * 获取在这次的盘点过程中还未盘点的库存记录
     * @param recordDTO
     * @param checkId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:query')")
    @GetMapping("/unCheckRecordList")
    public Result unCheckRecordList(RecordDTO recordDTO, Long checkId) {
        Page<RecordDTO> page = recordService.getUnCheckRecordList(recordDTO, checkId);
        return Result.success(PageUtils.convertPageResult(page));
    }

    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @PutMapping("/addCheckRecordAll")
    public Result addCheckRecordAll(Long checkId, Long[] recordIds) {
        inventoryCheckDetailService.addCheckRecordAll(checkId, recordIds);
        return Result.success();
    }
}
