package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.User;

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
    Page<Role> getPage(Role role);

    Set<String> getRolePermission(User user);
}
