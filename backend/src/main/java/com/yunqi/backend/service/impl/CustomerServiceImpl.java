package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.mapper.CustomerMapper;
import com.yunqi.backend.model.dto.CustomerDTO;
import com.yunqi.backend.model.entity.Customer;
import com.yunqi.backend.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户服务类
 * @author liyunqi
 */
@Service
@Transactional
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Resource
    CustomerMapper customerMapper;

    @Override
    public Page<Customer> getCustomerPage(CustomerDTO customerDTO) {
        Page<Customer> page = PageUtils.getPage();
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(customerDTO.getName() != null, Customer::getName, customerDTO.getName());
        wrapper.like(customerDTO.getNickname() != null, Customer::getNickname, customerDTO.getNickname());
        wrapper.like(customerDTO.getPhone() != null, Customer::getPhone, customerDTO.getPhone());
        return customerMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customerMapper.insert(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customerMapper.updateById(customer);
    }
}
