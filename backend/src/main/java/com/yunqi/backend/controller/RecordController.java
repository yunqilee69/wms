package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.RecordDTO;
import com.yunqi.backend.model.dto.WareDTO;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.RecordService;
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
    @PutMapping("/alarmThreshold")
    public Result updateAlarmThreshold(Long[] recordIds, Integer alarmThreshold) {
        recordService.updateAlarmThreshold(Arrays.asList(recordIds), alarmThreshold);
        return Result.success();
    }

}
