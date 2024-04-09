package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.RecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/inventory/record")
public class RecordController {

    @Resource
    RecordService recordService;

    /**
     * 分页查询
     * @param recordDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:record:list')")
    @GetMapping("/list")
    public Result getList(RecordDTO recordDTO) {
        Page<RecordDTO> page = recordService.getRecordPage(recordDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 更新库存报警阈值
     * @param recordIds
     * @param alarmThreshold
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:record:setAlarm')")
    @PutMapping("/alarmThreshold")
    public Result updateAlarmThreshold(Long[] recordIds, Integer alarmThreshold) {
        recordService.updateAlarmThreshold(Arrays.asList(recordIds), alarmThreshold);
        return Result.success();
    }

    /**
     * 新增库存
     * @param recordDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:record:add')")
    @PostMapping
    public Result add(@RequestBody RecordDTO recordDTO) {
        recordService.saveRecord(recordDTO);
        return Result.success();
    }

    /**
     * 删除库存记录
     * @param ids
     * @return
     */
    @PreAuthorize("@sps.hasPermi('inventory:record:delete')")
    @DeleteMapping
    public Result delete(List<Long> ids) {
        if (ids.size() == 0) {
            return Result.fail("库存id为空");
        }
        recordService.deleteRecord(ids);
        return Result.success();
    }
}
