package com.yunqi.backend.core.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.mapper.InventoryInfoMapper;
import com.yunqi.backend.mapper.RecordMapper;
import com.yunqi.backend.model.entity.InventoryInfo;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，系统的所有定时任务都在这里进行编写
 * @author liyunqi
 */
@Component
@Slf4j
@Transactional
public class ScheduleTask {

    @Resource
    RecordMapper recordMapper;

    @Resource
    InventoryInfoMapper inventoryInfoMapper;

    /**
     * 每天的11点半，执行更新仓库的信息
     */
    @Scheduled(cron = "0 30 23 * * *") // 每晚11点半执行
    //@Scheduled(cron = "0 0/2 * * * *")
    void updateInventoryInfo() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalNumber = 0;

        List<Record> recordList = recordMapper.selectList(null);
        for (Record record : recordList) {
            totalNumber += record.getNumber();
            totalAmount = totalAmount.add(record.getTotalAmount());
        }

        InventoryInfo inventoryInfo = new InventoryInfo();
        inventoryInfo.setTotalAmount(totalAmount);
        inventoryInfo.setTotalNumber(totalNumber);
        inventoryInfo.setRecordTime(LocalDateTime.now());

        inventoryInfoMapper.insert(inventoryInfo);
        log.info("仓库信息更新成功");
    }

}
