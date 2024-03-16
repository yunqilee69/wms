import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询货物列表
export function getWareList(query) {
    return request({
        url: '/inventory/ware/list',
        method: 'get',
        params: query
    })
}

// 查询货物详细
export function getWareById(wareId) {
    return request({
        url: '/inventory/ware/' + parseStrEmpty(wareId),
        method: 'get'
    })
}

// 新增货物
export function addWare(data) {
    return request({
        url: '/inventory/ware',
        method: 'post',
        data: data
    })
}

// 修改货物
export function updateWare(data) {
    return request({
        url: '/inventory/ware',
        method: 'put',
        data: data
    })
}

// 删除货物
export function delWare(wareId) {
    return request({
        url: '/inventory/ware/' + wareId,
        method: 'delete'
    })
}

export function getWareSelect() {
    return request({
        url: '/inventory/ware/getSelect',
        method: 'get'
    })
}