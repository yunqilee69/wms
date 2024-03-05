import request from '@/utils/request.js'
import { parseStrEmpty } from "@/utils/ruoyi.js";

// 查询员工列表
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

// 用户密码重置
export function resetUserPwd(userId) {
  return request({
    url: '/system/user/resetPwd/' + userId,
    method: 'put'
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
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

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword, confirmPassword) {
  const data = {
    oldPassword,
    newPassword,
    confirmPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    params: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/system/user/authRole',
    method: 'put',
    params: data
  })
}
