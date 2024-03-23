package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.WareService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 货物信息控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/inventory/ware")
public class WareController {

    @Resource
    WareService wareService;

    /**
     * 分页查询
     * @param wareDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresInfo:list')")
    @GetMapping("/list")
    public Result getList(WareDTO wareDTO) {
        Page<Ware> page = wareService.getWarePage(wareDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param wareId
     * @return
     */
    @PreAuthorize("@sps.hasAnyPermi('inventory:waresInfo:query,inventory:record:query')")
    @GetMapping("/{wareId}")
    public Result getOne(@PathVariable Long wareId) {
        Ware ware = wareService.getById(wareId);
        return Result.success(ware);
    }

    /**
     * 新增
     * @param wareDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresInfo:add')")
    @PostMapping
    public Result add(@RequestBody WareDTO wareDTO) {
        wareService.saveWare(wareDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param wareIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresInfo:delete')")
    @DeleteMapping("/{wareIds}")
    public Result delete(@PathVariable List<Long> wareIds) {
        if (wareIds == null || wareIds.size() == 0) {
            return Result.fail("货物id不能为空");
        }
        // TODO 删除前需要检验，库存记录是否存在对应的货物
        wareService.removeBatchByIds(wareIds);
        return Result.success();
    }

    /**
     * 更新
     * @param wareDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresInfo:edit')")
    @PutMapping
    public Result update(@RequestBody WareDTO wareDTO) {
        wareService.updateWare(wareDTO);
        return Result.success();
    }

    /**
     * 获取数据库所有的货物数据，并组装为前端select组件的数据
     * @return
     */
    @GetMapping("/getSelect")
    public Result getSelect() {
        List<Ware> wareList = wareService.getBaseMapper().selectList(null);
        Map<String, List<Ware>> map = wareList.stream().collect(Collectors.groupingBy(Ware::getBrand));
        List<Object> result = new ArrayList<>();
        map.forEach((key, value) -> {
            String brand = DictUtils.getLabelByValue(key, "sys_inventory_brand");
            // 将规格的值转换为显示名称
            value.forEach(ware -> {
                ware.setSpec(DictUtils.getLabelByValue(ware.getSpec(), "sys_inventory_spec"));
            });
            // 组装前端数据
            HashMap<Object, Object> t = new HashMap<>();
            t.put("label", brand);
            t.put("options", value);
            result.add(t);
        });
        return Result.success(result);
    }

    // TODO 需要新增一个方法，通过货物id查看货物的金额变化趋势，在表格上设计一个按钮，使用echarts进行显示，一个折线图中直接显示进价和售价

}
