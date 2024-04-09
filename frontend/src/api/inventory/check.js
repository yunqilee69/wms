import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询库存列表
export function getCheckList(query) {
    return request({
        url: '/inventory/check/list',
        method: 'get',
        params: query
    })
}

// 查询库存盘点详细
export function getCheckById(checkId) {
    return request({
        url: '/inventory/check/' + parseStrEmpty(checkId),
        method: 'get'
    })
}

// 新增库存盘点
export function addCheck(data) {
    return request({
        url: '/inventory/check',
        method: 'post',
        data: data
    })
}

// 修改库存盘点
export function updateCheck(data) {
    return request({
        url: '/inventory/check',
        method: 'put',
        data: data
    })
}

// 删除库存盘点
export function delCheck(checkId) {
    return request({
        url: '/inventory/check/' + checkId,
        method: 'delete'
    })
}

// 应用盘点单
export function applyCheck(checkId) {
    return request({
        url: '/inventory/check/apply',
        method: 'put',
        data: checkId
    })
}