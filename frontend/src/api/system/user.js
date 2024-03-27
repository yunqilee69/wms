import request from '@/utils/request.js'

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/profile',
    method: 'put',
    data: data
  })
}

// 用户密码更新
export function updateUserPwd(oldPassword, newPassword, confirmPassword) {
  const data = {
    oldPassword,
    newPassword,
    confirmPassword
  }
  return request({
    url: '/profile/updatePwd',
    method: 'put',
    params: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/profile/avatar',
    method: 'post',
    data: data
  })
}