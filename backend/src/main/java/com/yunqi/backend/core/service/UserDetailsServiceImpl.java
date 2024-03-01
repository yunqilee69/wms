package com.yunqi.backend.core.service;

import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.UserError;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.RoleService;
import com.yunqi.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Resource
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectUserByUsername(username);
        if (user == null) {
            throw new BizException(UserError.USERNAME_NOT_EXISTS);
        }
        return createLoginUser(user);
    }

        public UserDetails createLoginUser(User user) {
        // 设置LoginUserDTO的值
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        Set<String> roles = roleService.getRolePermission(user);
        Set<String> menuPermission = permissionService.getMenuPermission(user);

        loginUserDTO.setUserId(user.getId());
        loginUserDTO.setRoles(roles);
        loginUserDTO.setPermissions(menuPermission);
        loginUserDTO.setUser(user);
        return loginUserDTO;
    }
}
