package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.EmpDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.AliyunService;
import com.yunqi.backend.service.UserRoleService;
import com.yunqi.backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/user/emp")
public class EmpController {

    @Resource
    UserService userService;

    @Resource
    UserRoleService userRoleService;


    @Resource
    AliyunService aliyunService;

    /**
     * 分页查询获取员工
     * @param empDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('user:emp:list')")
    @GetMapping("/list")
    public Result getEmpList(EmpDTO empDTO) {
        Page<EmpDTO> page = userService.getEmpPage(empDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('user:emp:query')")
    @GetMapping("/{userId}")
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
    @PreAuthorize("@sps.hasPermi('user:emp:add')")
    @PostMapping
    public Result saveEmp(@RequestBody EmpDTO empDTO) {
        userService.saveEmp(empDTO);
        return Result.success();
    }

    /**
     * 更新员工
     * @param empDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('user:emp:edit')")
    @PutMapping
    public Result updateEmp(@RequestBody EmpDTO empDTO) {
        userService.updateEmp(empDTO);
        return Result.success();
    }

    /**
     * 根据用户id删除员工
     * @param userIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('user:emp:delete')")
    @DeleteMapping("/{userIds}")
    public Result deleteEmp(@PathVariable List<Long> userIds) {
        userService.deleteEmp(userIds);
        return Result.success();
    }

    /**
     * 重置员工密码
     * @param userId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('user:emp:resetPwd')")
    @PutMapping("/resetPwd/{userId}")
    public Result resetPwd(@PathVariable Long userId) {
        String password = SecurityUtils.generatePassword();
        userService.resetPwd(userId, password);

        // 发送新密码到手机号
        User user = userService.getById(userId);
        aliyunService.sendResetPwd(user.getPhone(), user.getNickname(), password);
        return Result.success();
    }

}
