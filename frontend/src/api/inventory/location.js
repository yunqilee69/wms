import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询货位列表
export function getLocationList(query) {
    return request({
        url: '/inventory/location/list',
        method: 'get',
        params: query
    })
}

// 查询货位详细
export function getLocationById(locationId) {
    return request({
        url: '/inventory/location/' + parseStrEmpty(locationId),
        method: 'get'
    })
}

// 新增货位
export function addLocation(data) {
    return request({
        url: '/inventory/location',
        method: 'post',
        data: data
    })
}

// 修改货位
export function updateLocation(data) {
    return request({
        url: '/inventory/location',
        method: 'put',
        data: data
    })
}

// 删除货位
export function delLocation(locationId) {
    return request({
        url: '/inventory/location/' + locationId,
        method: 'delete'
    })
}

// 获取货位选择器
export function getWareLocationSelect() {
    return request({
        url: '/inventory/location/getSelect',
        method: 'get'
    })
}