package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.mapper.WareMoneyMapper;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.model.entity.WareMoney;
import com.yunqi.backend.service.WareService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Resource
    WareMoneyMapper wareMoneyMapper;

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
        wareService.deleteWare(wareIds);
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

    /**
     * 根据货物id，获取货物价格的变更记录，以折线图表示出来
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:waresInfo:showMoney')")
    @GetMapping("/getMoneyChange")
    public Result getMoneyChange(Long id) {
        if (id == null) {
            return Result.fail("id不能为空");
        }
        // 获取数据
        LambdaQueryWrapper<WareMoney> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WareMoney::getWareId, id);
        List<WareMoney> wareMoneyList = wareMoneyMapper.selectList(wrapper);

        // 组装数据
        List<BigDecimal> salePriceList = new ArrayList<>();
        List<BigDecimal> purchasePriceList = new ArrayList<>();
        List<LocalDateTime> dateList = new ArrayList<>();
        for (WareMoney wareMoney : wareMoneyList) {
            salePriceList.add(wareMoney.getSalePrice());
            purchasePriceList.add(wareMoney.getPurchasePrice());
            dateList.add(wareMoney.getRecordTime());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("salePrice", salePriceList);
        result.put("purchasePrice", purchasePriceList);
        result.put("dateList", dateList);
        return Result.success(result);
    }
}
