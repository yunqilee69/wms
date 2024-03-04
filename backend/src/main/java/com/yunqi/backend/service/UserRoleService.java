package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.UserRoleDTO;
import com.yunqi.backend.model.entity.UserRole;

import java.util.List;

/**
 * @author liyunqi
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户id和角色id删除用户与角色之间的关系
     * @param userRoleDTO
     */
    void deleteAuthUser(UserRoleDTO userRoleDTO);

    /**
     * 批量删除用户与角色之间的关系
     * @param roleId
     * @param userIds
     */
    void deleteAuthUsers(Long roleId, List<Long> userIds);

    /**
     * 批量新增用户与角色之间的关系
     * @param roleId
     * @param userIds
     */
    void insertUserRole(Long roleId, List<Long> userIds);
}
