import request from '@/utils/request'

// 获取采购额
export const getPurchaseMoney = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/purchaseMoney',
        method: 'get',
        params: data
    })
}

// 获取销售额
export const getSaleMoney = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/saleMoney',
        method: 'get',
        params: data
    })
}

// 获取库存总数量
export const getInventoryNumber = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/inventoryNumber',
        method: 'get',
        params: data
    })
}

// 获取库存总价值
export const getInventoryAmount = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/inventoryAmount',
        method: 'get',
        params: data
    })
}

// 获取销量top10
export const getSalesTop10 = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/salesTop10',
        method: 'get',
        params: data
    })
}

// 获取利润top10
export const getProfitTop10 = (begin, end) => {
    const data = {
        begin,end
    }
    return request({
        url: '/dataBoard/profitTop10',
        method: 'get',
        params: data
    })
}