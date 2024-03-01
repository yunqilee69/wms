package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.RoleError;
import com.yunqi.backend.mapper.RoleMapper;
import com.yunqi.backend.model.dto.RoleDTO;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMapper roleMapper;

    @Override
    public Page<Role> getRolePage(RoleDTO roleDTO) {
        Page<Role> page = PageUtils.getPage();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(roleDTO.getName() != null, Role::getName, roleDTO.getName());
        wrapper.like(roleDTO.getRoleKey() != null, Role::getRoleKey, roleDTO.getRoleKey());
        wrapper.eq(roleDTO.getStatus() != null, Role::getStatus, roleDTO.getStatus());
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

    @Override
    public void saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        // TODO 完成校验规则

        save(role);
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        // TODO 完成校验规则

        updateById(role);
    }

    @Override
    public void deleteRoleByIds(List<Long> roleIds) {
        removeByIds(roleIds);
    }

    @Override
    public void changeStatus(RoleDTO roleDTO) {
        if (roleDTO.getId() == null) {
            throw new BizException(RoleError.ID_IS_EMPTY);
        }

        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Role::getId, roleDTO.getId());
        wrapper.set(roleDTO.getStatus() != null, Role::getStatus, roleDTO.getStatus());
        roleMapper.update(wrapper);
    }
}
