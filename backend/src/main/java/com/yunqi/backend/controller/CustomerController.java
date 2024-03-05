package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.CustomerDTO;
import com.yunqi.backend.model.entity.Customer;
import com.yunqi.backend.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/user/customer")
public class CustomerController {

    @Resource
    CustomerService customerService;

    /**
     * 分页查询
     * @param customerDTO
     * @return
     */
    @GetMapping("/list")
    public Result getList(CustomerDTO customerDTO) {
        Page<Customer> page = customerService.getCustomerPage(customerDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据id获取实体类
     * @param customerId
     * @return
     */
    @GetMapping("/{customerId}")
    public Result getOne(@PathVariable Long customerId) {
        Customer customer = customerService.getById(customerId);
        return Result.success(customer);
    }

    /**
     * 新增
     * @param customerDTO
     * @return
     */
    @PostMapping
    public Result add(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        return Result.success();
    }

    /**
     * 删除
     * @param customerIds
     * @return
     */
    @DeleteMapping("/{customerIds}")
    public Result delete(@PathVariable List<Long> customerIds) {
        if (customerIds == null || customerIds.size() == 0) {
            return Result.fail("客户id不能为空");
        }
        customerService.removeBatchByIds(customerIds);
        return Result.success();
    }

    /**
     * 更新
     * @param customerDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerDTO);
        return Result.success();
    }

}
