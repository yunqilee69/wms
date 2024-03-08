package com.yunqi.backend.core.service;

import com.yunqi.backend.common.constant.SystemConstants;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.core.security.context.PermissionContextHolder;
import com.yunqi.backend.model.dto.LoginUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 用于SpringSecurity的@PreAuthorize注解使用
 * 例如：@PreAuthorize("@sps.hasPermi('system:dict:export')")
 * 表示使用下面这个sps类的hasPermi方法，当结果为true才会执行使用注解的方法
 *
 * @author liyunqi
 */
@Service("sps")
public class SecurityPermissionService {
    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        if (loginUserDTO == null || CollectionUtils.isEmpty(loginUserDTO.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUserDTO.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return hasPermi(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        if (loginUserDTO == null || CollectionUtils.isEmpty(loginUserDTO.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUserDTO.getPermissions();
        for (String permi : permissions.split(",")) {
            if (permi != null && hasPermissions(authorities, permi)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        if (loginUserDTO == null || CollectionUtils.isEmpty(loginUserDTO.getRoleKeys())) {
            return false;
        }
        for (String roleKey : loginUserDTO.getRoleKeys()) {
            if (roleKey.equals(StringUtils.trim(role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUserDTO loginUserDTO = SecurityUtils.getLoginUser();
        if (loginUserDTO == null || CollectionUtils.isEmpty(loginUserDTO.getRoleKeys())) {
            return false;
        }
        for (String role : roles.split(",")) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(SystemConstants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
