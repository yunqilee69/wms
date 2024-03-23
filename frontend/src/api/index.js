import request from "@/utils/request.js";

// 获取处于报警状态的货物
export function getAlarmRecord() {
    return request({
        url: '/index/getAlarmRecord',
        method: 'get'
    })
}

// 获取处于未结算状态的订单
export function getAlarmOrder() {
    return request({
        url: '/index/getAlarmOrder',
        method: 'get'
    })
}