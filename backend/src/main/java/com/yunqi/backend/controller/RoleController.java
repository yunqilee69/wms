package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.service.RoleService;
import com.yunqi.backend.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public Result<PageResult> getRolePage(Role role) {
        Page<Role> page = roleService.getPage(role);
        return Result.success(PageUtils.convertPageResult(page));
    }

}
