package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.SupplierDTO;
import com.yunqi.backend.model.entity.Supplier;
import com.yunqi.backend.service.SupplierService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供应商控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/user/supplier")
public class SupplierController {

    @Resource
    SupplierService supplierService;

    /**
     * 分页查询
     * @param supplierDTO
     * @return
     */
    @GetMapping("/list")
    public Result getList(SupplierDTO supplierDTO) {
        Page<Supplier> page = supplierService.getSupplierPage(supplierDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param supplierId
     * @return
     */
    @GetMapping("/{supplierId}")
    public Result getOne(@PathVariable Long supplierId) {
        Supplier supplier = supplierService.getById(supplierId);
        return Result.success(supplier);
    }

    /**
     * 新增
     * @param supplierDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody SupplierDTO supplierDTO) {
        supplierService.saveSupplier(supplierDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param supplierIds
     * @return
     */
    @DeleteMapping("/{supplierIds}")
    public Result delete(@PathVariable List<Long> supplierIds) {
        if (supplierIds == null || supplierIds.size() == 0) {
            return Result.fail("货物id不能为空");
        }
        supplierService.removeBatchByIds(supplierIds);
        return Result.success();
    }

    /**
     * 更新
     * @param supplierDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(supplierDTO);
        return Result.success();
    }

}
