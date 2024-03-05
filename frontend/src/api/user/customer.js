import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询客户列表
export function getCustomerList(query) {
    return request({
        url: '/user/customer/list',
        method: 'get',
        params: query
    })
}

// 查询客户详细
export function getCustomerById(customerId) {
    return request({
        url: '/user/customer/' + parseStrEmpty(customerId),
        method: 'get'
    })
}

// 新增客户
export function addCustomer(data) {
    return request({
        url: '/user/customer',
        method: 'post',
        data: data
    })
}

// 修改客户
export function updateCustomer(data) {
    return request({
        url: '/user/customer',
        method: 'put',
        data: data
    })
}

// 删除客户
export function delCustomer(customerId) {
    return request({
        url: '/user/customer/' + customerId,
        method: 'delete'
    })
}
