package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.mapper.RoleMapper;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liyunqi
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMapper roleMapper;

    @Override
    public Page<Role> getPage(Role role) {
        Page<Role> page = PageUtils.getPage();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(role.getName() != null, Role::getName, role.getName());
        wrapper.eq(role.getStatus() != null, Role::getStatus, role.getStatus());
        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    public Set<String> getRolePermission(User user) {
        String username = user.getUsername();
        HashSet<String> result = new HashSet<>();
        if (username.equals("admin")) {
            result.add("admin");
        } else {
            result.addAll(roleMapper.getRolePermissionByUserId(user.getId()));
        }
        return result;
    }
}
