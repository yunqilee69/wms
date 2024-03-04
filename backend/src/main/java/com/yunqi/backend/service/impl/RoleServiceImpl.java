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
import com.yunqi.backend.model.entity.RoleMenu;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleMenuService;
import com.yunqi.backend.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMapper roleMapper;

    @Resource
    RoleMenuService roleMenuService;

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
    public Set<String> getRoleName(User user) {
        String username = user.getUsername();
        HashSet<String> result = new HashSet<>();
        if (username.equals("admin")) {
            result.add("管理员");
        } else {
            result.addAll(roleMapper.getRoleNameByUserId(user.getId()));
        }
        return result;
    }

    @Override
    public Set<String> getRoleKey(User user) {
        String username = user.getUsername();
        HashSet<String> result = new HashSet<>();
        if (username.equals("admin")) {
            result.add("admin");
        } else {
            result.addAll(roleMapper.getRoleKeyByUserId(user.getId()));
        }
        return result;
    }

    @Override
    public void saveRole(RoleDTO roleDTO) {
        if (roleDTO.getName() == null || StringUtils.isEmpty(roleDTO.getName())) {
            throw new BizException(RoleError.NAME_IS_EMPTY);
        }
        if (roleDTO.getStatus() == null || StringUtils.isEmpty(roleDTO.getStatus())) {
            throw new BizException(RoleError.STATUS_IS_EMPTY);
        }
        if (roleDTO.getRoleKey() == null || StringUtils.isEmpty(roleDTO.getRoleKey())) {
            throw new BizException(RoleError.ROLE_KEY_IS_EMPTY);
        }
        if (roleDTO.getOrderNum() <= 0) {
            throw new BizException(RoleError.ORDER_NUM_GT_ZERO);
        }
        if (roleMapper.selectByName(roleDTO.getName()) != null) {
            throw new BizException(RoleError.NAME_ALERTER_EXIST);
        }
        if (roleMapper.selectByRoleKey(roleDTO.getRoleKey()) != null) {
            throw new BizException(RoleError.ROLE_KEY_ALERTER_EXIST);
        }

        // 保存角色
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        save(role);

        // 保存角色相关联的菜单id
        List<RoleMenu> roleMenuList = new ArrayList<>();
        List<Long> menuIds = roleDTO.getMenuIds();
        Long roleId = role.getId();
        for (Long menuId : menuIds) {
            RoleMenu t = new RoleMenu();
            t.setRoleId(roleId);
            t.setMenuId(menuId);
            roleMenuList.add(t);
        }
        roleMenuService.saveBatch(roleMenuList);

    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        if (roleDTO.getName() == null || StringUtils.isEmpty(roleDTO.getName())) {
            throw new BizException(RoleError.NAME_IS_EMPTY);
        }
        if (roleDTO.getStatus() == null || StringUtils.isEmpty(roleDTO.getStatus())) {
            throw new BizException(RoleError.STATUS_IS_EMPTY);
        }
        if (roleDTO.getRoleKey() == null || StringUtils.isEmpty(roleDTO.getRoleKey())) {
            throw new BizException(RoleError.ROLE_KEY_IS_EMPTY);
        }
        if (roleDTO.getOrderNum() <= 0) {
            throw new BizException(RoleError.ORDER_NUM_GT_ZERO);
        }
        if (!checkRoleNameUnique(roleDTO.getName(), roleDTO.getId())) {
            throw new BizException(RoleError.NAME_ALERTER_EXIST);
        }
        if (!checkRoleKeyUnique(roleDTO.getRoleKey(), roleDTO.getId())) {
            throw new BizException(RoleError.ROLE_KEY_ALERTER_EXIST);
        }

        // 更新角色
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        updateById(role);

        Long roleId = role.getId();
        // 更新角色菜单
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(queryWrapper);

        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : roleDTO.getMenuIds()) {
            RoleMenu t = new RoleMenu();
            t.setRoleId(roleId);
            t.setMenuId(menuId);
            roleMenuList.add(t);
        }
        roleMenuService.saveBatch(roleMenuList);

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

    /**
     * 校验角色名是否唯一
     * @param roleName
     * @return
     */
    private boolean checkRoleNameUnique(String roleName, Long roleId) {
        if (StringUtils.isEmpty(roleName)) {
            return false;
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, roleName);
        List<Role> roleList = roleMapper.selectList(wrapper);
        if (roleList.size() == 0) {
            return true;
        } else if (roleList.size() == 1) {
            Role role = roleList.get(0);
            if (role.getName().equals(roleName) && role.getId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验权限字符是否唯一
     * @return
     */
    private boolean checkRoleKeyUnique(String roleKey, Long roleId) {
        if (StringUtils.isEmpty(roleKey)) {
            return false;
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleKey, roleKey);
        List<Role> roleList = roleMapper.selectList(wrapper);
        if (roleList.size() == 0) {
            return true;
        } else if (roleList.size() == 1) {
            Role role = roleList.get(0);
            if (role.getRoleKey().equals(roleKey) && role.getId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }
}
