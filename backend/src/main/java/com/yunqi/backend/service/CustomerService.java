package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.CustomerDTO;
import com.yunqi.backend.model.entity.Customer;

/**
 * 客户服务类
 * @author liyunqi
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 分页查询
     * @param customerDTO
     * @return
     */
    Page<Customer> getCustomerPage(CustomerDTO customerDTO);

    /**
     * 新增
     * @param customerDTO
     */
    void saveCustomer(CustomerDTO customerDTO);

    /**
     * 更新
     * @param customerDTO
     */
    void updateCustomer(CustomerDTO customerDTO);
}
