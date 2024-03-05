package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.EmpDTO;
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

    /**
     * 根据用户id查询角色id列表
     * @param userId
     * @return
     */
    List<Long> getRoleIdsByUserId(Long userId);


    /**
     * 根据用户id删除用户所有的角色
     * @param userId
     */
    void deleteUserRoleByUserId(Long userId);
}
