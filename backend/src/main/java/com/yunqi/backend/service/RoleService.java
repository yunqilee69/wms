package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RoleDTO;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.User;

import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
public interface RoleService extends IService<Role> {

    /**
     * 角色分页查询
     * @param role
     * @return
     */
    Page<Role> getRolePage(RoleDTO roleDTO);

    Set<String> getRolePermission(User user);

    void saveRole(RoleDTO roleDTO);

    void updateRole(RoleDTO roleDTO);

    void deleteRoleByIds(List<Long> roleIds);

    /**
     * 根据id更新状态
     * @param roleDTO
     */
    void changeStatus(RoleDTO roleDTO);
}
