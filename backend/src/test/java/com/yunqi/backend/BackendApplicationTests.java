package com.yunqi.backend;

import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.config.properties.AliSmsProperties;
import com.yunqi.backend.core.service.LoginService;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.mapper.RoleMapper;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
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
    DictTypeService dictTypeService;

    @Autowired
    DictItemService dictItemService;

    @Autowired
    private MenuMapper menuMapper;

    @Resource
    UserRoleService userRoleService;

    @Resource
    AliSmsProperties aliSmsProperties;

    @Resource
    AliyunService aliyunService;

    @Resource
    RoleMapper roleMapper;

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
        DictType dictType = new DictType();
        dictType.setCode("test_code");
        dictType.setName("测试");
        dictType.setRemark("测试备注");
        dictType.setOrderNum(1);

        dictTypeService.save(dictType);
    }

    @Test
    void menuMapperTest() {
        List<Menu> menuChildren = menuMapper.getMenuChildren("0-1-2");
        System.out.println(menuChildren.size());
    }

    @Test
    void roleMenuTest() {
        List<Long> menuList = Arrays.asList(1L,2L,3L,4L,5L);
        Long roleId = 1000L;
        HashMap<String, Object> params = new HashMap<>();
        params.put("menuIds", menuList);
        params.put("roleId", roleId);
    }

    @Test
    void userRoleServiceTest() {
        userRoleService.deleteUserRoleByUserId(null);
    }

    @Test
    void AliyunSmsUtilsTest() {
        System.out.println(aliyunService.ossDelete("aa0e8d7c07a546148d3d36dff4f34402.png"));

    }
}
