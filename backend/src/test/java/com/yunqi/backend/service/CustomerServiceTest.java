package com.yunqi.backend.service;

import com.yunqi.backend.model.entity.Customer;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author liyunqi
 */
@SpringBootTest
public class CustomerServiceTest {

    @Resource
    CustomerService customerService;

    @Test
    void insertTest() {
        Customer customer = new Customer();
        customer.setNickname("客户1");
        customer.setPhone("13312341234");
        customer.setEmail("12312@qq.com");
        customer.setGender("0");
        customer.setAvatar("cs");
        customer.setAddress("地址");
        customerService.save(customer);
    }

}
