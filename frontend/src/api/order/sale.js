import request from '@/utils/request.js'
import {parseStrEmpty} from "@/utils/ruoyi.js";

// 查询订单列表
export function getOrderList(query) {
    return request({
        url: '/order/sale/list',
        method: 'get',
        params: query
    })
}

// 查询订单详细
export function getOrderById(orderId) {
    return request({
        url: '/order/sale/' + parseStrEmpty(orderId),
        method: 'get'
    })
}

// 新增订单
export function addOrder(data) {
    return request({
        url: '/order/sale',
        method: 'post',
        data: data
    })
}

// 确认收货
export function takeDelivery(orderId) {
    const data = {
        orderId
    }
    return request({
        url: '/order/sale/takeDelivery',
        method: 'put',
        params: data
    })
}

// 订单结算
export function settlement(data) {
    return request({
        url: '/order/sale/settlement',
        method: 'put',
        params: data
    })
}

// 获取支付记录
export function getPictures(orderId) {
    const params = {
        orderId: orderId
    }
    return request({
        url: '/order/sale/pictures',
        method: 'get',
        params: params
    })
}

// 删除订单
export function delOrder(orderId) {
    return request({
        url: '/order/sale/' + orderId,
        method: 'delete'
    })
}

// -----------订单细节
// 获取订单细节数据
export function getOrderDetailList(params) {
    return request({
        url: '/order/saleDetail/list',
        method: 'get',
        params: params
    })
}

// 删除订单细节数据
export function delDetail(detailId) {
    return request({
        url: '/order/saleDetail/' + detailId,
        method: 'delete'
    })
}

// 设置货物数量
export function setDetailNumber(data) {
    return request({
        url: '/order/saleDetail/setNumber',
        method: 'put',
        params: data
    })
}

// 获取未在该订单的货物记录
export function getUnSaleRecordList(query) {
    return request({
        url: '/order/saleDetail/getUnSaleRecordList',
        method: 'get',
        params: query
    })
}

// 添加货物记录
export function addUnSaleRecord(data) {
    return request({
        url: '/order/saleDetail/addUnSaleRecord',
        method: 'put',
        params: data
    })
}

