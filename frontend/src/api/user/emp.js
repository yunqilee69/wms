
// 查询员工列表
import request from "@/utils/request.js";
import {parseStrEmpty} from "@/utils/ruoyi.js";

export function listEmpUser(query) {
    return request({
        url: '/system/user/emp/list',
        method: 'get',
        params: query
    })
}

// 查询用户详细
export function getEmpUser(userId) {
    return request({
        url: '/system/user/emp/' + parseStrEmpty(userId),
        method: 'get'
    })
}

// 新增用户
export function addEmpUser(data) {
    return request({
        url: '/system/user/emp',
        method: 'post',
        data: data
    })
}

// 修改用户
export function updateEmpUser(data) {
    return request({
        url: '/system/user/emp',
        method: 'put',
        data: data
    })
}

// 删除用户
export function delEmpUser(userId) {
    return request({
        url: '/system/user/emp/' + userId,
        method: 'delete'
    })
}

// 客户密码重置
export function resetUserPwd(userId) {
    return request({
        url: '/system/user/emp/resetPwd/' + userId,
        method: 'put'
    })
}

// 用户状态修改
export function changeEmpStatus(userId, status) {
    const data = {
        userId,
        status
    }
    return request({
        url: '/system/user/changeStatus',
        method: 'put',
        data: data
    })
}