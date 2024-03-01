package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.core.service.LoginService;
import com.yunqi.backend.core.service.PermissionService;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.request.LoginRequest;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.MenuService;
import com.yunqi.backend.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping
public class AccountController {

    @Resource
    LoginService loginService;

    @Resource
    UserService userService;

    @Resource
    PermissionService permissionService;

    @Resource
    MenuService menuService;

    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getCode(), loginRequest.getUuid());
        return Result.success(token);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Result<Map> getInfo() {
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", loginUserDTO.getUser());
        map.put("permissions", loginUserDTO.getPermissions());
        map.put("roles", loginUserDTO.getRoles());
        return Result.success(map);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public Result getRouters() {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        List<Menu> menus = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menuService.buildMenus(menus));
    }

    /**
     * 注册用户
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterRequest registerRequest) {
        Long userId = userService.registerUser(registerRequest);
        return Result.success("注册成功",userId);
    }
}
