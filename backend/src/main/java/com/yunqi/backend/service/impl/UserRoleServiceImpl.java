package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.UserError;
import com.yunqi.backend.mapper.UserRoleMapper;
import com.yunqi.backend.model.dto.EmpDTO;
import com.yunqi.backend.model.dto.UserRoleDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.entity.UserRole;
import com.yunqi.backend.service.UserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    UserRoleMapper userRoleMapper;

    @Override
    public void deleteAuthUser(UserRoleDTO userRoleDTO) {
        if (userRoleDTO.getUserId() == null || userRoleDTO.getRoleId() == null) {
            throw new  BizException(UserError.USER_ROLE_ID_NULL);
        }

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, userRoleDTO.getRoleId());
        wrapper.eq(UserRole::getUserId, userRoleDTO.getUserId());

        userRoleMapper.delete(wrapper);
    }

    @Override
    public void deleteAuthUsers(Long roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.size() == 0) {
            throw new  BizException(UserError.USER_ROLE_ID_NULL);
        }

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        wrapper.in(UserRole::getUserId, userIds);

        userRoleMapper.delete(wrapper);
    }

    @Override
    public void insertUserRole(Long roleId, List<Long> userIds) {
        if (roleId == null || userIds == null || userIds.size() == 0) {
            throw new  BizException(UserError.USER_ROLE_ID_NULL);
        }

        ArrayList<UserRole> userRoleList = new ArrayList<>();
        for (Long userId : userIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }

        saveBatch(userRoleList);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        return userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public void deleteUserRoleByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, UserRole::getUserId, userId);
        remove(wrapper);
    }

}
