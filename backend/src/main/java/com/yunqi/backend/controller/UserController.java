package com.yunqi.backend.controller;

import cn.hutool.core.util.IdUtil;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.AliyunService;
import com.yunqi.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/profile")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    AliyunService aliyunService;

    /**
     * 获取用户个人信息
     * @return
     */
    @GetMapping
    public Result getProfile() {
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        User user = userService.getById(loginUserDTO.getUserId());
        user.setPassword("******");

        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roles", loginUserDTO.getRoleNames());
        return Result.success(map);
    }

    @PutMapping
    public Result updateProfile(@RequestBody UserDTO userDTO) {
        userService.updateProfile(userDTO);
        return Result.success();
    }

    /**
     * 更新密码
     */
    @PutMapping("/updatePwd")
    public Result updatePwd(UserDTO userDTO) {
        userService.updatePassword(userDTO);
        return Result.success();
    }

    @PostMapping("/avatar")
    public Result updateAvatar(MultipartFile avatarfile) throws IOException {
        String originalFilename = avatarfile.getOriginalFilename();
        String filename = IdUtil.simpleUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = aliyunService.ossUpload(avatarfile.getInputStream(), filename);
        // 更新头像
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        User user = userService.getById(loginUserDTO.getUserId());
        user.setAvatar(url);
        userService.saveOrUpdate(user);
        return Result.success(url);
    }

}
