package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.EmpDTO;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.entity.UserRole;
import com.yunqi.backend.service.AliyunSmsService;
import com.yunqi.backend.service.RoleService;
import com.yunqi.backend.service.UserRoleService;
import com.yunqi.backend.service.UserService;
import org.springframework.beans.BeanUtils;
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
    UserRoleService userRoleService;

    @Resource
    AliyunSmsService aliyunSmsService;

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

    /**
     * 分页查询获取员工
     * @param empDTO
     * @return
     */
    @GetMapping("/emp/list")
    public Result getEmpList(EmpDTO empDTO) {
        Page<EmpDTO> page = userService.getEmpPage(empDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/emp/{userId}")
    public Result getEmpById(@PathVariable Long userId) {
        User user = userService.selectUserById(userId);
        EmpDTO empDTO = new EmpDTO();
        BeanUtils.copyProperties(user, empDTO);
        empDTO.setPassword("******");
        empDTO.setUserId(userId);
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        empDTO.setRoleIds(roleIds);
        return Result.success(empDTO);
    }

    /**
     * 新增员工
     * @param empDTO
     * @return
     */
    @PostMapping("/emp")
    public Result saveEmp(@RequestBody EmpDTO empDTO) {
        userService.saveEmp(empDTO);
        return Result.success();
    }

    /**
     * 更新员工
     * @param empDTO
     * @return
     */
    @PutMapping("/emp")
    public Result updateEmp(@RequestBody EmpDTO empDTO) {
        userService.updateEmp(empDTO);
        return Result.success();
    }

    /**
     * 根据用户id删除员工
     * @param userId
     * @return
     */
    @DeleteMapping("/emp/{userIds}")
    public Result deleteEmp(@PathVariable List<Long> userIds) {
        userService.deleteEmp(userIds);
        return Result.success();
    }


    @PutMapping("/resetPwd/{userId}")
    public Result resetPwd(@PathVariable Long userId) {
        String password = SecurityUtils.generatePassword();
        userService.resetPwd(userId, password);

        // 发送新密码到手机号
        User user = userService.getById(userId);
        aliyunSmsService.sendResetPwd(user.getPhone(), user.getNickname(), password);
        return Result.success();
    }

}
