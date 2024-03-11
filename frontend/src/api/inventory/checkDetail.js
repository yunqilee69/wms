import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询库存列表
export function getCheckDetailList(query) {
    return request({
        url: '/inventory/checkDetail/list',
        method: 'get',
        params: query
    })
}

// 查询库存盘点详细
export function getCheckDetailById(checkDetailId) {
    return request({
        url: '/inventory/checkDetail/' + parseStrEmpty(checkDetailId),
        method: 'get'
    })
}

// 新增库存盘点
export function addCheckDetail(data) {
    return request({
        url: '/inventory/checkDetail',
        method: 'post',
        data: data
    })
}

// 修改库存盘点
export function updateCheckDetail(data) {
    return request({
        url: '/inventory/checkDetail',
        method: 'put',
        data: data
    })
}

// 删除库存盘点
export function delCheckDetail(checkDetailId) {
    return request({
        url: '/inventory/checkDetail/' + checkDetailId,
        method: 'delete'
    })
}

// 获取在这次的盘点过程中还未盘点的库存记录
export function unCheckRecordList(query) {
    return request({
        url: '/inventory/checkDetail/unCheckRecordList',
        method: 'get',
        params: query
    })
}

// 添加选中的待盘点的数据记录
export function addCheckRecordAll(data) {
    return request({
        url: '/inventory/checkDetail/addCheckRecordAll',
        method: 'put',
        params: data
    })
}
