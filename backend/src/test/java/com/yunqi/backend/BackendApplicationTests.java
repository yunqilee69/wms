package com.yunqi.backend;

import cn.hutool.core.util.IdUtil;
import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.core.service.LoginService;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.model.dto.RegisterUserDTO;
import com.yunqi.backend.model.entity.DictCode;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.DictCodeService;
import com.yunqi.backend.service.DictItemService;
import com.yunqi.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    LoginService loginService;

    @Autowired
    DictCodeService dictCodeService;

    @Autowired
    DictItemService dictItemService;

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void registerTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        //registerRequest.setUsername(IdUtil.simpleUUID());
        registerRequest.setUsername("admin");
        registerRequest.setPassword("admin123");
        registerRequest.setConfirmPassword("admin123");
        registerRequest.setPhone("17330721036");
        userService.registerUser(registerRequest);
    }

    @Test
    void redisTest() {
        redisCache.setCacheObject("test1", "你好");
    }

    @Test
    void selectUserByUsernameTest() {
        System.out.println(userService.selectUserByUsername("1"));
    }

    @Test
    void loginTest() {
        redisCache.setCacheObject(CacheConstants.CAPTCHA_CODE_KEY+"1234", "1234");
        loginService.login("123456", "123456", "1234", "1234");
        System.out.println(SecurityUtils.getLoginUser());
    }

    @Test
    void genPasswordTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String jiami = passwordEncoder.encode("123456");
        System.out.println(passwordEncoder.matches("123456", "$2a$10$ZrqWjvbKE6r9YMWTCJ6DduTxHpXuVdBDR.VmKuB.XPC8/g6caINNm"));
        System.out.println(jiami);

    }

    @Test
    void serviceTest() {
        DictCode dictCode = new DictCode();
        dictCode.setCode("test_code");
        dictCode.setName("测试");
        dictCode.setRemark("测试备注");
        dictCode.setOrderNum(1);
        dictCode.setStatus(1);

        dictCodeService.save(dictCode);
    }

    @Test
    void menuMapperTest() {
        List<Menu> menuChildren = menuMapper.getMenuChildren("0-1-2");
        System.out.println(menuChildren.size());
    }
}
