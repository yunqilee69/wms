package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.InventoryCheckDTO;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.InventoryCheck;
import com.yunqi.backend.service.InventoryCheckService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 盘点库
 * @author liyunqi
 */
@RestController
@RequestMapping("/inventory/check")
public class InventoryCheckController {

    @Resource
    InventoryCheckService inventoryCheckService;

    /**
     * 分页查询
     * @param inventoryCheckDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:list')")
    @GetMapping("/list")
    public Result getList(InventoryCheckDTO inventoryCheckDTO) {
        Page<InventoryCheck> page = inventoryCheckService.getInventoryCheckPage(inventoryCheckDTO);
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
        InventoryCheck inventoryCheck = inventoryCheckService.getById(inventoryCheckId);
        return Result.success(inventoryCheck);
    }

    /**
     * 新增
     * @param inventoryCheckDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:add')")
    @PostMapping
    public Result add(@RequestBody InventoryCheckDTO inventoryCheckDTO) {
        inventoryCheckService.saveInventoryCheck(inventoryCheckDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param inventoryCheckIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:delete')")
    @DeleteMapping("/{inventoryCheckIds}")
    public Result delete(@PathVariable List<Long> inventoryCheckIds) {
        if (inventoryCheckIds == null || inventoryCheckIds.size() == 0) {
            return Result.fail("库存盘点id不能为空");
        }
        inventoryCheckService.deleteInventoryCheck(inventoryCheckIds);
        return Result.success();
    }

    /**
     * 更新
     * @param inventoryCheckDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:edit')")
    @PutMapping
    public Result update(@RequestBody InventoryCheckDTO inventoryCheckDTO) {
        inventoryCheckService.updateInventoryCheck(inventoryCheckDTO);
        return Result.success();
    }

//-------------------------------盘点细节---------------------------------------

    /**
     * 获取
     * @param wareDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:check:query')")
    @GetMapping("/detail/list")
    public Result getCheckDetailList(WareDTO wareDTO, Long checkId) {
        return Result.success();
    }

    // TODO 将盘点的数据应用到数据库中，应用后不可修改
}
