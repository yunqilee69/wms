import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询库存列表
export function getRecordList(query) {
    return request({
        url: '/inventory/record/list',
        method: 'get',
        params: query
    })
}

// 设置货物报警阈值
export function updateAlarmThreshold(recordIds, alarmThreshold) {
    const data = {
        recordIds: recordIds,
        alarmThreshold: alarmThreshold
    }
    return request({
        url: '/inventory/record/alarmThreshold',
        method: 'put',
        params: data
    })
}