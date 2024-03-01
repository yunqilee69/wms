package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.RoleDTO;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    RoleService roleService;

    @GetMapping("/list")
    public Result<PageResult> getRolePage(RoleDTO roleDTO) {
        Page<Role> page = roleService.getRolePage(roleDTO);
        return Result.success(PageUtils.convertPageResult(page));
    }

    @GetMapping("/{roleId}")
    public Result<Role> getRoleById(@PathVariable Long roleId) {
        return Result.success(roleService.getById(roleId));
    }

    @PostMapping
    public Result saveRole(@Validated @RequestBody RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);
        return Result.success();
    }

    @PutMapping
    public Result updateRole(@Validated @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return Result.success();
    }

    /**
     * 根据id更新状态
     * @param roleDTO
     * @return
     */
    @PutMapping("/changeStatus")
    public Result changeStatus(@RequestBody RoleDTO roleDTO) {
        roleService.changeStatus(roleDTO);
        return Result.success();
    }

    @DeleteMapping("/{roleIds}")
    public Result deleteRoleByIds(@PathVariable List<Long> roleIds) {
        roleService.deleteRoleByIds(roleIds);
        return Result.success();
    }

}
