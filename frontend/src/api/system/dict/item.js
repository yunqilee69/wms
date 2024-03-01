import request from '@/utils/request'

// 查询字典数据列表
export function listItem(query) {
  return request({
    url: '/system/dict/item/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getItem(typeCode) {
  return request({
    url: '/system/dict/item/' + typeCode,
    method: 'get'
  })
}

// 根据字典类型编码查询字典数据信息
export function getDicts(dictItem) {
  return request({
    url: '/system/dict/item/code/' + dictItem,
    method: 'get'
  })
}

// 新增字典数据
export function addItem(data) {
  return request({
    url: '/system/dict/item',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateItem(data) {
  return request({
    url: '/system/dict/item',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delItem(dictCode) {
  return request({
    url: '/system/dict/item/' + dictCode,
    method: 'delete'
  })
}
