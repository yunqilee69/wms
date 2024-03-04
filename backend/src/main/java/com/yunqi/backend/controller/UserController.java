package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleService;
import com.yunqi.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    RoleService roleService;

    /**
     * 获取用户个人信息
     * @return
     */
    @GetMapping("/profile")
    public Result getProfile() {
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        User user = userService.getById(loginUserDTO.getUserId());
        user.setPassword("******");

        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roles", loginUserDTO.getRoleNames());
        return Result.success(map);
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserDTO userDTO) {
        userService.updateProfile(userDTO);
        return Result.success();
    }

    /**
     * 更新密码
     */
    @PutMapping("/profile/updatePwd")
    public Result updatePwd(UserDTO userDTO) {
        userService.updatePassword(userDTO);
        return Result.success();
    }

    @GetMapping("/emp/list")
    public Result getEmpList() {
        return Result.success();
    }
}
