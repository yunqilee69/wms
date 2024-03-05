import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询客户列表
export function getSupplierList(query) {
    return request({
        url: '/user/supplier/list',
        method: 'get',
        params: query
    })
}

// 查询客户详细
export function getSupplierById(supplierId) {
    return request({
        url: '/user/supplier/' + parseStrEmpty(supplierId),
        method: 'get'
    })
}

// 新增客户
export function addSupplier(data) {
    return request({
        url: '/user/supplier',
        method: 'post',
        data: data
    })
}

// 修改客户
export function updateSupplier(data) {
    return request({
        url: '/user/supplier',
        method: 'put',
        data: data
    })
}

// 删除客户
export function delSupplier(supplierId) {
    return request({
        url: '/user/supplier/' + supplierId,
        method: 'delete'
    })
}
